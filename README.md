# Dentix - Backend

API REST do Dentix, um sistema de gestão para clínica odontológica desenvolvido como projeto final do programa Wise Start.

O backend foi construído com Java e Spring Boot, utilizando autenticação JWT, controle de permissões por perfil, persistência com Spring Data JPA e banco de dados MySQL. A API fornece os recursos necessários para gerenciamento de usuários, pacientes, dentistas, especialidades, consultas, relatórios, notificações, perfil, autenticação em dois fatores e financeiro.

## Aplicação em produção

```text
https://dentix.app.br
```

A API é consumida em produção pelo frontend por meio do caminho `/api`, com Nginx na EC2 fazendo o encaminhamento para o container do backend Spring Boot.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Security
- Bean Validation
- MySQL
- Hibernate
- JWT com JJWT
- BCrypt
- ZXing
- Maven
- Docker
- Docker Compose

## Funcionalidades principais

- Autenticação com JWT.
- Controle de acesso por perfil `ADMIN` e `DENTISTA`.
- Cadastro, listagem, edição, desativação e exclusão lógica de usuários.
- Cadastro, listagem, edição, desativação, reativação e remoção de pacientes.
- Cadastro, listagem, edição, desativação, reativação e remoção de dentistas.
- Vinculação de dentistas com múltiplas especialidades.
- Cadastro, listagem, edição e remoção de especialidades.
- Criação, edição, cancelamento e finalização de consultas.
- Validação para impedir agendamento em datas passadas.
- Validação para impedir conflito de horário para o mesmo dentista.
- Validação para garantir que o horário final seja posterior ao horário inicial.
- Cancelamento de consulta com motivo obrigatório.
- Relatórios de consultas com filtros por paciente, dentista, especialidade, usuário responsável e período.
- Paginação em endpoints de listagem.
- Proteção de rotas sensíveis com Spring Security.
- Tratamento centralizado de erros.
- Validação de dados com Bean Validation.

## Extras implementados

- Autenticação em dois fatores com código de 6 dígitos.
- Geração de QR Code para aplicativo autenticador usando ZXing.
- Criptografia do segredo do 2FA antes de salvar no banco.
- Foto de perfil vinculada ao usuário.
- Endpoints para alterar e remover foto de perfil.
- Administração da foto de perfil de dentistas pelo admin.
- Notificações internas para usuários.
- Solicitações de alteração de dados com resposta administrativa.
- Módulo financeiro com lançamentos vinculados às consultas.
- Resumo financeiro com valores recebidos, pendentes, pagos e cancelados.
- Dados institucionais da clínica.
- Dockerfile para containerizar a API.
- Docker Compose de produção com MySQL, backend e frontend.
- Guia de deploy em EC2 com Nginx e Certbot.

## Requisitos

Para execução local sem Docker:

- Java 17
- Maven
- MySQL

Para execução com Docker:

- Docker
- Docker Compose

## Variáveis de ambiente

A aplicação utiliza variáveis de ambiente para dados sensíveis:

```env
MYSQL_USER=usuario_do_banco
MYSQL_PASSWORD=senha_do_banco
JWT_SECRET=chave_secreta_para_assinatura_jwt
APP_CRYPTO_SECRET_KEY=chave_base64_de_32_bytes_para_criptografia
```

No deploy com Docker Compose também são utilizadas:

```env
MYSQL_ROOT_PASSWORD=senha_root_mysql
MYSQL_DATABASE=sistema_gestao_consultas
FRONTEND_PATH=/caminho/para/o/frontend
```

Existe um arquivo `.env.example` com um modelo das variáveis esperadas.

## Banco de dados

O projeto utiliza MySQL e espera um banco chamado:

```text
sistema_gestao_consultas
```

Em desenvolvimento e deploy, o schema é gerado pelo Hibernate/JPA com:

```yaml
spring.jpa.hibernate.ddl-auto=update
```

O arquivo abaixo contém uma massa de dados de demonstração:

```text
src/main/resources/data.sql
```

A senha padrão dos usuários inseridos pelo `data.sql` é:

```text
123456
```

Exemplo de usuário admin:

```text
lorenzo.andreoli@dentix.com.br
123456
```

## Execução local

Configure o banco MySQL e as variáveis de ambiente. Depois execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Build

Para gerar o arquivo `.jar`:

```bash
./mvnw -DskipTests package
```

No Windows:

```bash
mvnw.cmd -DskipTests package
```

O artefato será criado em:

```text
target/gestao-consultas-0.0.1-SNAPSHOT.jar
```

## Docker

Construir a imagem do backend:

```bash
docker build -t dentix-backend .
```

Subir a aplicação completa com MySQL, backend e frontend:

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Verificar containers:

```bash
docker ps
```

Ver logs do backend:

```bash
docker logs dentix-backend --tail 100
```

## Deploy em EC2

O projeto possui um guia específico de deploy:

```text
DEPLOY_EC2.md
```

A arquitetura prevista é:

```text
Internet
  |
  v
Nginx no host EC2 com HTTPS
  |
  v
Container frontend Nginx
  |
  v
/api -> Container backend Spring Boot
  |
  v
Container MySQL
```

No deploy recomendado, apenas as portas `80` e `443` ficam públicas. O backend e o MySQL permanecem internos na rede Docker.

Aplicação publicada:

```text
https://dentix.app.br
```

## Endpoints principais

### Autenticação

```text
POST /auth/login
```

### Usuários

```text
POST   /usuarios
GET    /usuarios
GET    /usuarios/me
GET    /usuarios/{id}
PUT    /usuarios/{id}
PUT    /usuarios/{id}/desativar
DELETE /usuarios/{id}
PUT    /usuarios/me/foto
DELETE /usuarios/me/foto
PUT    /usuarios/{id}/foto
DELETE /usuarios/{id}/foto
POST   /usuarios/me/2fa/setup
POST   /usuarios/me/2fa/confirm
DELETE /usuarios/me/2fa
```

### Pacientes

```text
POST   /pacientes
GET    /pacientes
GET    /pacientes/paginado
GET    /pacientes/{id}
PUT    /pacientes/{id}
PUT    /pacientes/{id}/desativar
PUT    /pacientes/{id}/reativar
DELETE /pacientes/{id}
```

### Dentistas

```text
POST   /dentistas
GET    /dentistas
GET    /dentistas/paginado
GET    /dentistas/{id}
PUT    /dentistas/{id}
PUT    /dentistas/{id}/desativar
PUT    /dentistas/{id}/reativar
DELETE /dentistas/{id}
```

### Especialidades

```text
POST   /especialidades
GET    /especialidades
GET    /especialidades/{id}
PUT    /especialidades/{id}
DELETE /especialidades/{id}
```

### Consultas

```text
POST /consultas
GET  /consultas
GET  /consultas/paginado
PUT  /consultas/{id}/editar
PUT  /consultas/{id}/cancelar
PUT  /consultas/{id}/finalizar
GET  /consultas/relatorios
```

### Financeiro

```text
GET  /financeiro/lancamentos
GET  /financeiro/resumo
GET  /financeiro/lancamentos/{id}
POST /financeiro/lancamentos
PUT  /financeiro/lancamentos/{id}/pagar
PUT  /financeiro/lancamentos/{id}/cancelar
```

### Notificações

```text
GET /notificacoes
GET /notificacoes/nao-lidas/total
PUT /notificacoes/{id}/lida
PUT /notificacoes/lidas
```

### Solicitações de alteração

```text
POST /solicitacoes-alteracao
GET  /solicitacoes-alteracao/minhas
GET  /solicitacoes-alteracao/admin
GET  /solicitacoes-alteracao/admin/pendentes
POST /solicitacoes-alteracao/{id}/responder
```

### Clínica

```text
GET /clinica
PUT /clinica
```

## Estrutura principal

```text
src/main/java/com/lorenzo/gestaoconsultas/config
```

Configurações de segurança, JWT, filtro de autenticação, CORS e criptografia.

```text
src/main/java/com/lorenzo/gestaoconsultas/controller
```

Controllers REST da aplicação.

```text
src/main/java/com/lorenzo/gestaoconsultas/entity
```

Entidades JPA mapeadas para o banco de dados.

```text
src/main/java/com/lorenzo/gestaoconsultas/repository
```

Repositories Spring Data JPA.

```text
src/main/java/com/lorenzo/gestaoconsultas/service
```

Regras de negócio e integração entre controllers, repositories e entidades.

```text
src/main/resources
```

Configurações da aplicação e script de dados de demonstração.

## Autor

Lorenzo Carneiro Andreoli