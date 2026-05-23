# Exemplos da API AutoBots - ATVIII

Este guia reflete a API atual da ATVIII. Os exemplos abaixo usam `http://localhost:8080` como base e mostram os recursos do novo dominio, incluindo respostas HATEOAS.

## Convencoes adotadas

- Cabecalho sugerido: `Content-Type: application/json`
- Rotas protegidas exigem `Authorization: Bearer <token>`
- Datas podem ser enviadas no formato ISO-8601
- Respostas de `GET`, `POST` e `PUT` retornam `_links`
- Respostas de `DELETE` retornam `204 No Content`
- IDs mostrados aqui sao ilustrativos

## Fluxo-base recomendado

1. Autenticar em `POST /login`
2. Cadastrar uma empresa
3. Cadastrar usuarios com perfis `ADMINISTRADOR`, `GERENTE`, `VENDEDOR` e `CLIENTE`
4. Cadastrar mercadorias e servicos
5. Registrar uma venda ligando empresa, cliente, vendedor, veiculo e itens

## Autenticacao

### POST /login

```http
POST /login
Content-Type: application/json
```

```json
{
  "nomeUsuario": "admin",
  "senha": "senha123"
}
```

Resposta esperada:

- Status `200 OK`
- Header `Authorization: Bearer <token>`

Exemplo de uso nas proximas chamadas:

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## 1. Empresa

### POST /empresas

```http
POST /empresas
Content-Type: application/json
```

```json
{
  "razaoSocial": "AutoBots Comercio e Servicos Ltda",
  "nomeFantasia": "AutoBots Center",
  "endereco": {
    "estado": "SP",
    "cidade": "Sao Jose dos Campos",
    "bairro": "Centro",
    "rua": "Avenida Andromeda",
    "numero": "500",
    "codigoPostal": "12230000",
    "informacoesAdicionais": "Sala 12"
  },
  "telefones": [
    {
      "ddd": "12",
      "numero": "39450000"
    }
  ]
}
```

Exemplo de resposta `201 Created`:

```json
{
  "id": 1,
  "razaoSocial": "AutoBots Comercio e Servicos Ltda",
  "nomeFantasia": "AutoBots Center",
  "cadastro": "2026-05-07T21:30:00.000+00:00",
  "endereco": {
    "id": 1,
    "estado": "SP",
    "cidade": "Sao Jose dos Campos",
    "bairro": "Centro",
    "rua": "Avenida Andromeda",
    "numero": "500",
    "codigoPostal": "12230000",
    "informacoesAdicionais": "Sala 12"
  },
  "telefones": [
    {
      "id": 1,
      "ddd": "12",
      "numero": "39450000"
    }
  ],
  "usuariosIds": [],
  "mercadoriasIds": [],
  "servicosIds": [],
  "vendasIds": [],
  "_links": {
    "self": {
      "href": "http://localhost:8080/empresas/1"
    },
    "empresas": {
      "href": "http://localhost:8080/empresas"
    },
    "endereco": {
      "href": "http://localhost:8080/empresas/1/endereco"
    },
    "telefones": {
      "href": "http://localhost:8080/empresas/1/telefones"
    },
    "usuarios": {
      "href": "http://localhost:8080/empresas/1/usuarios"
    },
    "mercadorias": {
      "href": "http://localhost:8080/empresas/1/mercadorias"
    },
    "servicos": {
      "href": "http://localhost:8080/empresas/1/servicos"
    },
    "vendas": {
      "href": "http://localhost:8080/empresas/1/vendas"
    },
    "atualizar": {
      "href": "http://localhost:8080/empresas/1"
    },
    "remover": {
      "href": "http://localhost:8080/empresas/1"
    }
  }
}
```

### GET /empresas

Retorna uma colecao com `_embedded` e link `cadastrar`.

### GET /empresas/1

Retorna o mesmo recurso HATEOAS acima.

### PUT /empresas/1

```json
{
  "razaoSocial": "AutoBots Operacoes Ltda",
  "nomeFantasia": "AutoBots Prime",
  "endereco": {
    "estado": "SP",
    "cidade": "Sao Jose dos Campos",
    "bairro": "Jardim Aquarius",
    "rua": "Rua das Oficinas",
    "numero": "700",
    "codigoPostal": "12246021",
    "informacoesAdicionais": "Fundos"
  }
}
```

### DELETE /empresas/1

Remove a empresa.

## 2. Endereco e telefones da empresa

### GET /empresas/1/endereco

Retorna o endereco da empresa com links `self`, `empresa`, `atualizar` e `remover`.

### POST /empresas/1/endereco

Use este endpoint quando a empresa nao tiver endereco cadastrado.

```json
{
  "estado": "SP",
  "cidade": "Taubate",
  "bairro": "Independencia",
  "rua": "Rua Um",
  "numero": "100",
  "codigoPostal": "12031000",
  "informacoesAdicionais": "Galpao B"
}
```

### GET /empresas/1/telefones

Lista telefones da empresa.

### POST /empresas/1/telefones

```json
{
  "ddd": "12",
  "numero": "988776655"
}
```

### PUT /empresas/1/telefones/1

```json
{
  "ddd": "12",
  "numero": "39451234"
}
```

### DELETE /empresas/1/telefones/1

Remove o telefone.

## 3. Usuario cliente

### POST /empresas/1/usuarios

```http
POST /empresas/1/usuarios
Content-Type: application/json
```

```json
{
  "nome": "Marina Oliveira",
  "nomeSocial": "Marina O.",
  "dataNascimento": "1994-02-10T00:00:00.000+00:00",
  "perfis": [
    "CLIENTE"
  ],
  "endereco": {
    "estado": "SP",
    "cidade": "Sao Jose dos Campos",
    "bairro": "Urbanova",
    "rua": "Rua das Palmeiras",
    "numero": "45",
    "codigoPostal": "12244000",
    "informacoesAdicionais": "Casa 2"
  },
  "telefones": [
    {
      "ddd": "12",
      "numero": "997001122"
    }
  ],
  "documentos": [
    {
      "tipo": "CPF",
      "dataEmissao": "2020-01-15T00:00:00.000+00:00",
      "numero": "12345678900"
    }
  ],
  "emails": [
    {
      "endereco": "marina@email.com"
    }
  ],
  "credenciais": [
    {
      "tipo": "usuario_senha",
      "nomeUsuario": "marina.oliveira",
      "senha": "Senha@123"
    }
  ],
  "veiculos": [
    {
      "tipo": "SUV",
      "modelo": "Jeep Compass",
      "placa": "ABC1D23"
    }
  ]
}
```

Exemplo de resposta `201 Created`:

```json
{
  "id": 10,
  "empresaId": 1,
  "nome": "Marina Oliveira",
  "nomeSocial": "Marina O.",
  "dataNascimento": "1994-02-10T00:00:00.000+00:00",
  "dataCadastro": "2026-05-07T21:40:00.000+00:00",
  "perfis": [
    "CLIENTE"
  ],
  "endereco": {
    "id": 20,
    "estado": "SP",
    "cidade": "Sao Jose dos Campos",
    "bairro": "Urbanova",
    "rua": "Rua das Palmeiras",
    "numero": "45",
    "codigoPostal": "12244000",
    "informacoesAdicionais": "Casa 2"
  },
  "telefones": [
    {
      "id": 30,
      "ddd": "12",
      "numero": "997001122"
    }
  ],
  "documentos": [
    {
      "id": 40,
      "tipo": "CPF",
      "dataEmissao": "2020-01-15T00:00:00.000+00:00",
      "numero": "12345678900"
    }
  ],
  "emails": [
    {
      "id": 50,
      "endereco": "marina@email.com"
    }
  ],
  "credenciais": [
    {
      "id": 60,
      "tipo": "usuario_senha",
      "criacao": "2026-05-07T21:40:00.000+00:00",
      "ultimoAcesso": null,
      "inativo": false,
      "nomeUsuario": "marina.oliveira",
      "senha": "Senha@123",
      "codigo": null
    }
  ],
  "veiculos": [
    {
      "id": 70,
      "tipo": "SUV",
      "modelo": "Jeep Compass",
      "placa": "ABC1D23"
    }
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/usuarios/10"
    },
    "usuarios": {
      "href": "http://localhost:8080/empresas/1/usuarios"
    },
    "endereco": {
      "href": "http://localhost:8080/usuarios/10/endereco"
    },
    "telefones": {
      "href": "http://localhost:8080/usuarios/10/telefones"
    },
    "documentos": {
      "href": "http://localhost:8080/usuarios/10/documentos"
    },
    "emails": {
      "href": "http://localhost:8080/usuarios/10/emails"
    },
    "credenciais": {
      "href": "http://localhost:8080/usuarios/10/credenciais"
    },
    "veiculos": {
      "href": "http://localhost:8080/usuarios/10/veiculos"
    },
    "atualizar": {
      "href": "http://localhost:8080/usuarios/10"
    },
    "remover": {
      "href": "http://localhost:8080/usuarios/10"
    }
  }
}
```

### GET /empresas/1/usuarios

Lista os usuarios da empresa.

### GET /usuarios/10

Busca um usuario especifico.

### PUT /usuarios/10

Atualizacao parcial dos campos principais:

```json
{
  "nomeSocial": "Marina Oliveira Silva",
  "perfis": [
    "CLIENTE",
    "VENDEDOR"
  ]
}
```

### DELETE /usuarios/10

Remove o usuario.

## 4. Subrecursos do usuario

Todos os subrecursos seguem o padrao:

- `GET /usuarios/{usuarioId}/{subrecurso}`
- `POST /usuarios/{usuarioId}/{subrecurso}`
- `GET /usuarios/{usuarioId}/{subrecurso}/{id}` quando aplicavel
- `PUT /usuarios/{usuarioId}/{subrecurso}/{id}` quando aplicavel
- `DELETE /usuarios/{usuarioId}/{subrecurso}/{id}` quando aplicavel

### Endereco do usuario

```json
{
  "estado": "SP",
  "cidade": "Jacarei",
  "bairro": "Centro",
  "rua": "Rua das Flores",
  "numero": "80",
  "codigoPostal": "12300000",
  "informacoesAdicionais": "Apartamento 11"
}
```

### Telefone do usuario

```json
{
  "ddd": "11",
  "numero": "998887766"
}
```

### Documento do usuario

```json
{
  "tipo": "CNH",
  "dataEmissao": "2023-09-01T00:00:00.000+00:00",
  "numero": "99887766554"
}
```

### Email do usuario

```json
{
  "endereco": "marina.oliveira@cliente.com"
}
```

### Credencial do usuario por nome e senha

```json
{
  "tipo": "usuario_senha",
  "nomeUsuario": "marina.login",
  "senha": "Senha@456"
}
```

### Credencial do usuario por codigo de barras

```json
{
  "tipo": "codigo_barra",
  "codigo": 1234567890123
}
```

### Veiculo do usuario

```json
{
  "tipo": "HATCH",
  "modelo": "Onix LT",
  "placa": "BRA2E19"
}
```

## 5. Usuario vendedor

### POST /empresas/1/usuarios

```json
{
  "nome": "Carlos Santos",
  "nomeSocial": "Carlos",
  "dataNascimento": "1988-11-21T00:00:00.000+00:00",
  "perfis": [
    "VENDEDOR"
  ],
  "emails": [
    {
      "endereco": "carlos@autobots.com"
    }
  ],
  "credenciais": [
    {
      "tipo": "codigo_barra",
      "codigo": 998877665544
    }
  ]
}
```

Use um vendedor quando a venda precisar de `funcionarioId`.

## 6. Mercadoria

### POST /empresas/1/mercadorias

```json
{
  "validade": "2027-12-31T00:00:00.000+00:00",
  "fabricao": "2026-04-01T00:00:00.000+00:00",
  "nome": "Filtro de Oleo Premium",
  "quantidade": 25,
  "valor": 39.9,
  "descricao": "Filtro de oleo para motores flex",
  "fornecedorId": 12
}
```

Exemplo de resposta:

```json
{
  "id": 100,
  "empresaId": 1,
  "fornecedorId": 12,
  "validade": "2027-12-31T00:00:00.000+00:00",
  "fabricao": "2026-04-01T00:00:00.000+00:00",
  "cadastro": "2026-05-07T21:50:00.000+00:00",
  "nome": "Filtro de Oleo Premium",
  "quantidade": 25,
  "valor": 39.9,
  "descricao": "Filtro de oleo para motores flex",
  "_links": {
    "self": {
      "href": "http://localhost:8080/empresas/1/mercadorias/100"
    },
    "mercadorias": {
      "href": "http://localhost:8080/empresas/1/mercadorias"
    },
    "atualizar": {
      "href": "http://localhost:8080/empresas/1/mercadorias/100"
    },
    "remover": {
      "href": "http://localhost:8080/empresas/1/mercadorias/100"
    }
  }
}
```

### PUT /empresas/1/mercadorias/100

```json
{
  "quantidade": 20,
  "valor": 42.5,
  "descricao": "Filtro de oleo com nova tabela"
}
```

## 7. Servico

### POST /empresas/1/servicos

```json
{
  "nome": "Troca de oleo",
  "valor": 120.0,
  "descricao": "Servico com filtro e mao de obra"
}
```

### PUT /empresas/1/servicos/200

```json
{
  "valor": 135.0,
  "descricao": "Servico com revisao de nivel"
}
```

## 8. Venda

### POST /empresas/1/vendas

```json
{
  "identificacao": "VENDA-2026-0001",
  "clienteId": 10,
  "funcionarioId": 11,
  "veiculoId": 70,
  "mercadoriasIds": [
    100
  ],
  "servicosIds": [
    200
  ]
}
```

Exemplo de resposta:

```json
{
  "id": 300,
  "empresaId": 1,
  "cadastro": "2026-05-07T22:00:00.000+00:00",
  "identificacao": "VENDA-2026-0001",
  "clienteId": 10,
  "funcionarioId": 11,
  "veiculoId": 70,
  "mercadoriasIds": [
    100
  ],
  "servicosIds": [
    200
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/empresas/1/vendas/300"
    },
    "vendas": {
      "href": "http://localhost:8080/empresas/1/vendas"
    },
    "atualizar": {
      "href": "http://localhost:8080/empresas/1/vendas/300"
    },
    "remover": {
      "href": "http://localhost:8080/empresas/1/vendas/300"
    }
  }
}
```

### PUT /empresas/1/vendas/300

```json
{
  "identificacao": "VENDA-2026-0001-A",
  "clienteId": 10,
  "funcionarioId": 11,
  "veiculoId": 70,
  "mercadoriasIds": [
    100
  ],
  "servicosIds": [
    200
  ]
}
```

## 9. Respostas de erro

### Validacao `400 Bad Request`

```json
{
  "timestamp": "2026-05-07T22:05:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "razaoSocial: razaoSocial deve ser informada",
  "caminho": "/empresas"
}
```

### Recurso ausente `404 Not Found`

```json
{
  "timestamp": "2026-05-07T22:06:00",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Empresa nao encontrada",
  "caminho": "/empresas/999"
}
```

### Conflito `409 Conflict`

```json
{
  "timestamp": "2026-05-07T22:07:00",
  "status": 409,
  "erro": "Conflict",
  "mensagem": "Ja existe documento cadastrado com o numero informado",
  "caminho": "/usuarios/10/documentos"
}
```

## 10. Resumo rapido dos endpoints

| Recurso | Rotas principais |
| --- | --- |
| Empresa | `/empresas`, `/empresas/{empresaId}` |
| Endereco da empresa | `/empresas/{empresaId}/endereco` |
| Telefones da empresa | `/empresas/{empresaId}/telefones`, `/empresas/{empresaId}/telefones/{telefoneId}` |
| Usuarios da empresa | `/empresas/{empresaId}/usuarios`, `/usuarios/{usuarioId}` |
| Endereco do usuario | `/usuarios/{usuarioId}/endereco` |
| Telefones do usuario | `/usuarios/{usuarioId}/telefones`, `/usuarios/{usuarioId}/telefones/{telefoneId}` |
| Documentos do usuario | `/usuarios/{usuarioId}/documentos`, `/usuarios/{usuarioId}/documentos/{documentoId}` |
| Emails do usuario | `/usuarios/{usuarioId}/emails`, `/usuarios/{usuarioId}/emails/{emailId}` |
| Credenciais do usuario | `/usuarios/{usuarioId}/credenciais`, `/usuarios/{usuarioId}/credenciais/{credencialId}` |
| Veiculos do usuario | `/usuarios/{usuarioId}/veiculos`, `/usuarios/{usuarioId}/veiculos/{veiculoId}` |
| Mercadorias | `/empresas/{empresaId}/mercadorias`, `/empresas/{empresaId}/mercadorias/{mercadoriaId}` |
| Servicos | `/empresas/{empresaId}/servicos`, `/empresas/{empresaId}/servicos/{servicoId}` |
| Vendas | `/empresas/{empresaId}/vendas`, `/empresas/{empresaId}/vendas/{vendaId}` |
