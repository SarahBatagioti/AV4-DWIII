![Banner AutoBots](assets/bannerAutobots.png)

O AutoBots é um sistema de gestão especializado para lojas de manutenção veicular e venda de autopeças. O projeto surge em um cenário de alta demanda e valorização do mercado de veículos usados no Brasil, onde a agilidade na gestão e a excelência no atendimento ao cliente tornaram-se diferenciais competitivos essenciais.

Nesta versão, o dominio legado centrado em `Cliente` foi substituido por um modelo mais amplo, com suporte a empresas, usuarios com perfis distintos, mercadorias, servicos, vendas e subrecursos relacionados. Todas as respostas principais seguem o estilo HATEOAS.

## Tecnologias

- Java 17
- Spring Boot 2.6.3
- Spring Web
- Spring Data JPA
- Spring HATEOAS
- Spring Validation
- Maven
- MySQL
- H2 (suporte em runtime e testes)

## Requisitos para rodar

- JDK 17 instalado e configurado no PATH
- MySQL Server (recomendado 8.x)
- Git
- Postman (Desktop ou Web)

## Como rodar o projeto

### 1) Clonar o repositório

```bash
git clone https://github.com/SarahBatagioti/AV3-DWIII
```

### 2) Entrar na pasta do projeto

```bash
cd AV3-DWIII/automanager
```

### 3) Configurar o banco MySQL

As configurações ficam em `src/main/resources/application.properties`.

Propriedades atuais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/AV3?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=fatec
spring.jpa.hibernate.ddl-auto=update
```

Ajuste `username` e `password` conforme o seu ambiente.

### 4) Rodar a aplicação

No Windows (PowerShell/CMD):

```bash
mvnw.cmd spring-boot:run
```

No Linux/macOS/Git Bash:

```bash
./mvnw spring-boot:run
```

Saída esperada:

- Aplicacao iniciada em `http://localhost:8080`
- Banco `AV3` criado automaticamente (se não existir)
- Um cliente de exemplo pode ser inserido automaticamente na primeira execução

## Como testar os endpoints da API

Para testar rapidamente pelo navegador, abra o Swagger em `http://localhost:8080/swagger-ui/index.html`, escolha o endpoint e clique em **Try it out**.

Para ver exemplos prontos de todas as requisições, consulte o guia detalhado:

👉 [Clique aqui para ver exemplos completos de todas as requisições](EXEMPLOS_API.md)

