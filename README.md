![Banner AutoBots](assets/bannerAutobots.png)

O AutoBots e um sistema de gestao especializado para lojas de manutencao veicular e venda de autopecas.

Nesta versao, o dominio legado centrado em cliente foi substituido por um modelo mais amplo, com suporte a empresas, usuarios com perfis distintos, mercadorias, servicos, vendas e subrecursos relacionados. Todas as respostas principais seguem o estilo HATEOAS e a API agora exige autenticacao com JWT.

## Tecnologias

- Java 17
- Spring Boot 2.6.3
- Spring Web
- Spring Data JPA
- Spring Security
- Spring HATEOAS
- Spring Validation
- Maven
- MySQL
- H2 (runtime e testes)

## Requisitos para rodar

- JDK 17 instalado e configurado no PATH
- MySQL Server (recomendado 8.x)
- Git
- Postman (Desktop ou Web)

## Como rodar o projeto

### 1) Clonar o repositorio

```bash
git clone https://github.com/SarahBatagioti/AV4-DWIII
```

### 2) Entrar na pasta do projeto

```bash
cd AV4-DWIII/automanager
```

### 3) Configurar o banco MySQL

As configuracoes ficam em `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/AV4?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=fatec
spring.jpa.hibernate.ddl-auto=update
jwt.secret=AV4-DWIII-segredo-jwt-autobots
jwt.expiration=600000
```

Ajuste `username`, `password` e `jwt.secret` conforme o seu ambiente.

### 4) Rodar a aplicacao

No Windows:

```bash
mvnw.cmd spring-boot:run
```

No Linux/macOS/Git Bash:

```bash
./mvnw spring-boot:run
```

## Como autenticar

Antes de acessar as rotas protegidas, envie uma requisicao para `POST /login`:

```json
{
  "nomeUsuario": "admin",
  "senha": "senha123"
}
```

A resposta devolve o token JWT no header `Authorization` no formato `Bearer <token>`.

Use esse header nas proximas requisicoes:

```http
Authorization: Bearer <token>
```

## Perfis suportados

- `ADMINISTRADOR`: CRUD total na aplicacao, incluindo usuarios administradores
- `GERENTE`: CRUD sobre gerente, vendedor e cliente, alem de mercadorias, servicos e vendas
- `VENDEDOR`: CRUD de clientes, leitura de mercadorias/servicos e criacao/leitura das proprias vendas
- `CLIENTE`: leitura apenas do proprio cadastro e das proprias vendas

## Como testar a API

Para testar rapidamente pelo navegador, abra o Swagger em `http://localhost:8080/swagger-ui/index.html`, escolha o endpoint e clique em **Try it out**.

Para ver exemplos prontos de requisicoes e do fluxo com autenticacao, consulte:

[EXEMPLOS_API.md](EXEMPLOS_API.md)
