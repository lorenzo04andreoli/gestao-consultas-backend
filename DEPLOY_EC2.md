# Deploy Dentix em EC2

Este guia descreve a arquitetura de deploy do Dentix usando Docker, Docker Compose, Nginx e Certbot em uma instancia EC2 Ubuntu.

Aplicacao publicada:

```text
https://dentix.app.br
https://www.dentix.app.br
```

## Arquitetura

```text
Internet
  |
  | HTTPS 443
  v
Nginx no host EC2
  |
  | proxy_pass http://127.0.0.1:8081
  v
Container frontend Nginx
  |
  | /api -> http://backend:8080
  v
Container backend Spring Boot
  |
  v
Container MySQL
```

Apenas as portas 80 e 443 devem ficar publicas. O backend e o MySQL ficam acessiveis somente pela rede Docker.

## Security Group da EC2

Liberar:

```text
22   SSH
80   HTTP
443  HTTPS
```

Nao liberar:

```text
8080
8081
3306
3307
```

No deploy final, o backend nao fica exposto diretamente na internet. O acesso a API acontece pelo caminho `/api`, passando primeiro pelo Nginx da EC2 e depois pelo Nginx do container frontend.

## Preparacao da EC2

Atualize a maquina:

```bash
sudo apt update && sudo apt upgrade -y
```

Instale Docker, Nginx e Certbot:

```bash
sudo apt install docker.io nginx certbot python3-certbot-nginx -y
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ubuntu
```

Depois faca logout/login no SSH para o grupo `docker` valer.

Em algumas imagens do Ubuntu, o pacote `docker-compose-plugin` pode nao estar disponivel pelo `apt`. Nesse caso, instale o Docker Compose pelo metodo oficial ou valide se ele ja esta instalado com:

```bash
docker --version
docker compose version
nginx -v
certbot --version
```

## Clonar repositorios

Os repositorios devem ficar lado a lado:

```bash
cd /home/ubuntu
git clone https://github.com/lorenzo04andreoli/gestao-consultas-backend.git gestao-consultas
git clone https://github.com/lorenzo04andreoli/gestao-consultas-frontend.git gestao-dental
```

## Variaveis de ambiente

No backend, crie o arquivo `.env` a partir do exemplo:

```bash
cd /home/ubuntu/gestao-consultas
cp .env.example .env
nano .env
```

Preencha senhas fortes e chaves reais.

Exemplo de variaveis esperadas:

```env
MYSQL_ROOT_PASSWORD=senha_root_mysql
MYSQL_DATABASE=sistema_gestao_consultas
MYSQL_USER=usuario_do_banco
MYSQL_PASSWORD=senha_do_banco
JWT_SECRET=chave_base64_para_jwt
APP_CRYPTO_SECRET_KEY=chave_base64_de_32_bytes
FRONTEND_PATH=/home/ubuntu/gestao-dental
```

Para gerar a chave `APP_CRYPTO_SECRET_KEY`:

```bash
openssl rand -base64 32
```

## Subir containers

Dentro do backend:

```bash
cd /home/ubuntu/gestao-consultas
docker compose -f docker-compose.prod.yml up -d --build
```

Verifique:

```bash
docker ps
docker logs dentix-backend --tail 100
```

O esperado em producao e algo semelhante a:

```text
dentix-frontend  127.0.0.1:8081->80/tcp
dentix-backend   8080/tcp
dentix-mysql     3306/tcp
```

O frontend fica acessivel apenas localmente na EC2 pela porta `8081`; o Nginx do host e quem publica a aplicacao em `80` e `443`.

## Popular banco de dados

Na primeira subida, o Hibernate cria as tabelas, mas a massa inicial pode precisar ser importada manualmente caso o banco esteja vazio.

Para importar o `data.sql`:

```bash
cd /home/ubuntu/gestao-consultas
docker exec -i dentix-mysql mysql -u root -p sistema_gestao_consultas < src/main/resources/data.sql
```

Para verificar usuarios cadastrados:

```bash
docker exec -it dentix-mysql mysql -u root -p sistema_gestao_consultas
```

Dentro do MySQL:

```sql
SELECT id, nome, email, perfil, ativo FROM usuarios;
```

Usuario admin de demonstracao:

```text
lorenzo.andreoli@dentix.com.br
123456
```

## Nginx do host

Crie o arquivo do site:

```bash
sudo nano /etc/nginx/sites-available/dentix
```

Conteudo inicial para HTTP:

```nginx
server {
    listen 80;
    server_name dentix.app.br www.dentix.app.br;

    location / {
        proxy_pass http://127.0.0.1:8081;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Ative o site:

```bash
sudo ln -s /etc/nginx/sites-available/dentix /etc/nginx/sites-enabled/dentix
sudo nginx -t
sudo systemctl reload nginx
```

Se aparecer a pagina padrao do Nginx, confira se o site `dentix` esta habilitado e se o proxy aponta para `127.0.0.1:8081`.

## HTTPS com Certbot

Antes, aponte o DNS do dominio para o IP publico da EC2.

Depois rode:

```bash
sudo certbot --nginx -d dentix.app.br -d www.dentix.app.br
```

Teste a renovacao automatica:

```bash
sudo certbot renew --dry-run
```

Valide a configuracao do Nginx:

```bash
sudo nginx -t
```

## CORS em producao

O backend precisa liberar as origens do dominio em `SecurityConfig`:

```text
https://dentix.app.br
https://www.dentix.app.br
```

Tambem e importante permitir os headers usados pelo navegador no preflight CORS, incluindo `content-type`.

Teste o preflight:

```bash
curl -i -X OPTIONS https://dentix.app.br/api/auth/login \
  -H "Origin: https://dentix.app.br" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: content-type"
```

Se retornar `Invalid CORS request`, confira se a EC2 esta com a branch atualizada e se o backend foi rebuildado.

## Atualizar deploy

Para atualizar depois de novos commits:

```bash
cd /home/ubuntu/gestao-consultas
git fetch origin
git checkout main
git pull --ff-only origin main

cd /home/ubuntu/gestao-dental
git fetch origin
git checkout main
git pull --ff-only origin main

cd /home/ubuntu/gestao-consultas
docker compose -f docker-compose.prod.yml up -d --build
```

Se houver alteracoes locais no `docker-compose.prod.yml` da EC2, preserve antes do pull:

```bash
git stash push -m "config deploy ec2" docker-compose.prod.yml
git pull --ff-only origin main
git stash pop
```

## Restart do frontend apos rebuild do backend

Depois de recriar o container do backend, pode acontecer `502 Bad Gateway` temporario porque o Nginx dentro do container frontend pode manter a resolucao antiga do servico `backend`.

Quando isso acontecer, reinicie o frontend:

```bash
docker compose -f docker-compose.prod.yml restart frontend
```

Depois teste novamente:

```bash
curl -i https://dentix.app.br
```

## Diagnostico rapido

Ver containers:

```bash
docker ps
```

Logs do backend:

```bash
docker logs dentix-backend --tail 100
```

Logs do frontend:

```bash
docker logs dentix-frontend --tail 100
```

Testar login direto pela API:

```bash
curl -i -X POST https://dentix.app.br/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"lorenzo.andreoli@dentix.com.br","senha":"123456"}'
```

## Backup do banco

Backup manual:

```bash
docker exec dentix-mysql mysqldump -u root -p sistema_gestao_consultas > backup_dentix.sql
```

Restaurar backup:

```bash
docker exec -i dentix-mysql mysql -u root -p sistema_gestao_consultas < backup_dentix.sql
```
