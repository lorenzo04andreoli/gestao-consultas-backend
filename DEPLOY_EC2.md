# Deploy Dentix em EC2

Este guia descreve a arquitetura de deploy do Dentix usando Docker, Nginx e Certbot em uma instancia EC2 Ubuntu.

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

## Preparacao da EC2

Atualize a maquina:

```bash
sudo apt update && sudo apt upgrade -y
```

Instale Docker:

```bash
sudo apt install docker.io docker-compose-plugin -y
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ubuntu
```

Depois faca logout/login no SSH para o grupo `docker` valer.

Instale Nginx e Certbot:

```bash
sudo apt install nginx certbot python3-certbot-nginx -y
```

## Clonar repositorios

Os repositorios devem ficar lado a lado:

```bash
cd /home/ubuntu
git clone <url-do-backend> gestao-consultas
git clone <url-do-frontend> gestao-dental
```

## Variaveis de ambiente

No backend, crie o arquivo `.env` a partir do exemplo:

```bash
cd /home/ubuntu/gestao-consultas
cp .env.example .env
nano .env
```

Preencha senhas fortes e chaves reais.

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

## Nginx do host

Crie o arquivo do site:

```bash
sudo nano /etc/nginx/sites-available/dentix
```

Conteudo inicial para HTTP:

```nginx
server {
    listen 80;
    server_name seu-dominio.com.br www.seu-dominio.com.br;

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

## HTTPS com Certbot

Antes, aponte o DNS do dominio para o IP publico da EC2.

Depois rode:

```bash
sudo certbot --nginx -d seu-dominio.com.br -d www.seu-dominio.com.br
```

Teste a renovacao automatica:

```bash
sudo certbot renew --dry-run
```

## Atualizar deploy

Para atualizar depois de novos commits:

```bash
cd /home/ubuntu/gestao-consultas
git pull
cd /home/ubuntu/gestao-dental
git pull
cd /home/ubuntu/gestao-consultas
docker compose -f docker-compose.prod.yml up -d --build
```

## Backup do banco

Backup manual:

```bash
docker exec dentix-mysql mysqldump -u root -p sistema_gestao_consultas > backup_dentix.sql
```