# Minpix - API de Contas Bancárias

## Visão geral
O Minpix é uma API REST para gestão de contas bancárias, com foco em segurança, organização da lógica de negócio e clareza na separação entre camadas da aplicação.

A solução foi pensada para seguir um fluxo simples e estruturado:

- autenticação via Auth0
- validação de token JWT
- processamento nas camadas Controller, Service e Repository
- regras de negócio para contas bancárias

## Regras de negócio
- Todas as contas possuem saldo e limite diário.
- Nome do titular e CPF são obrigatórios.
- Somente usuários autenticados podem acessar a API.
- Saldo e limite diário devem ser valores positivos.
- O cliente deve enviar o token no header da requisição para acessar endpoints protegidos.

## Fluxo da aplicação

```mermaid
flowchart LR
    A[Usuário] -->|1. Login com Auth0| B[Auth0]
    B -->|2. Retorna JWT| C[Cliente]
    C -->|3. Envia requisição com token| D[Controller]
    D -->|4. Valida token| E[Service]
    E -->|5. Aplica regras de negócio| F[Repository]
    F -->|6. Acesso aos dados| G[Persistência]
```

## Arquitetura
A aplicação foi organizada em camadas para facilitar manutenção e evolução:

- Controller: recebe as requisições HTTP e encaminha para a camada de serviço.
- Service: executa as regras de negócio e validações da aplicação.
- Repository: realiza acesso aos dados e comunicação com a persistência.

## Segurança
A API exige autenticação por token JWT para acesso às operações protegidas. Esse mecanismo garante que apenas usuários autorizados consigam consultar ou manipular informações sensíveis relacionadas às contas.

## Tecnologias
- Java 21
- Spring Boot 3 / 4
- Maven
- Auth0 para autenticação
- JWT para validação de acesso

## Estrutura do projeto

```text
minpix/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/minpix/
│   │   │       └── MinpixApplication.java
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/example/minpix/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── HELP.md
└── README.md
```

## Como executar o projeto

### Pré-requisitos
- Java 21+
- Maven
- Git

### Executando localmente
No diretório raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando o Maven instalado globalmente:

```bash
mvn spring-boot:run
```

A aplicação iniciará em um servidor local, normalmente na porta padrão do Spring Boot:

```text
http://localhost:8080
```

## Endpoints
A API ainda pode ser expandida com endpoints como:

- `POST /login` - autenticação do usuário
- `GET /contas` - listagem de contas
- `GET /contas/{id}` - busca por conta específica
- `POST /contas` - criação de conta
- `PUT /contas/{id}` - atualização de conta
- `DELETE /contas/{id}` - exclusão da conta

> Os endpoints e regras de payload podem ser ajustados de acordo com a evolução da aplicação.

## Observações
Este projeto foi estruturado como base para uma API bancária moderna, com foco em segurança, autenticidade e organização por responsabilidade de camada. A arquitetura permite evoluir facilmente para inclusão de banco de dados, testes automatizados e novos módulos.

## Licença
Este projeto está em desenvolvimento e pode ser adaptado conforme as necessidades do time e do cliente.
