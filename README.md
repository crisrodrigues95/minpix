# Minpix

Minpix é uma aplicação Java com Spring Boot para cadastro e consulta de contas bancárias, chaves Pix, favorecidos, transferências Pix e comprovantes. O projeto disponibiliza uma API REST com operações CRUD para essas entidades.

## Situação atual

Estão implementados:

- Entidades JPA e repositórios Spring Data JPA para contas, chaves Pix, favorecidos, transferências Pix e comprovantes.
- Serviços de persistência e controllers REST para listar, consultar por ID, cadastrar, atualizar e excluir registros.
- Documentação OpenAPI/Swagger dos endpoints.
- Cadastro de conta com preenchimento automático da data de cadastro.

As operações de transferência Pix atualmente apenas persistem os dados informados: não movimentam saldo, verificam limites, executam agendamentos nem integram com a infraestrutura do Pix. As demais datas, status e relacionamentos também precisam ser informados conforme os dados aceitos pelas entidades; não há regras gerais que os preencham automaticamente.

Os endpoints recebem e retornam as próprias entidades. Embora a dependência de Bean Validation esteja incluída, os controllers não usam `@Valid` e as entidades não definem restrições de validação. A aplicação exige um JWT emitido pelo Auth0 nas rotas REST, mas ainda não valida o audience configurado; portanto, a proteção está incompleta. Também não há DTOs nem regras completas de negócio. A aplicação é um projeto em desenvolvimento e não deve ser usada em produção ou com dados financeiros reais.

## API REST

Todos os recursos abaixo oferecem:

| Operação | Método e caminho | Resposta esperada |
|---|---|---|
| Listar | `GET /{recurso}` | `200 OK` com a lista |
| Consultar por ID | `GET /{recurso}/{id}` | `200 OK` ou `404 Not Found` |
| Cadastrar | `POST /{recurso}` | `201 Created` |
| Atualizar | `PUT /{recurso}/{id}` | `200 OK` ou `404 Not Found` |
| Excluir | `DELETE /{recurso}/{id}` | `204 No Content` ou `404 Not Found` |

Os recursos disponíveis são:

| Recurso | Caminho |
|---|---|
| Contas | `/contas` |
| Chaves Pix | `/chaves-pix` |
| Favorecidos | `/favorecidos` |
| Transferências Pix | `/transferencias-pix` |
| Comprovantes | `/comprovantes` |

Os corpos de cadastro e atualização são JSON com os campos das respectivas entidades. A documentação interativa do Swagger UI fica em `http://localhost:8080/swagger-ui/index.html`, e o documento OpenAPI em `http://localhost:8080/v3/api-docs`, após iniciar a aplicação. O Swagger UI e o documento OpenAPI podem ser abertos sem token; para executar operações da API pelo Swagger, use **Authorize** e informe um access token do Auth0.

### Auth0 e teste no Postman

1. No Auth0, registre uma API e configure seu **Identifier**. O valor deve corresponder a `auth0.audience` em `application.properties`.
2. Crie ou selecione uma aplicação **Machine to Machine** e autorize-a a acessar essa API.
3. Configure `spring.security.oauth2.resourceserver.jwt.issuer-uri` com o domínio do tenant Auth0, incluindo `https://` e a barra final. Não use o Client ID, Client Secret ou o Identifier da API nesse campo.
4. No Postman, solicite um token com o fluxo **Client Credentials**. Use `https://SEU-DOMINIO/oauth/token` como URL do token, as credenciais da aplicação M2M e o Identifier da API como **Audience**. Mantenha o Client Secret fora do repositório.
5. Envie o `access_token` como **Bearer Token** em uma chamada, por exemplo `GET http://localhost:8080/contas`.

**Limitação da configuração atual:** `auth0.audience` está definido, mas não é usado pelo `JwtDecoder`; o código valida o emissor/assinatura, mas não confirma se o token foi emitido para o Identifier desta API. Além disso, `POST /auth/login` exige autenticação e emite um JWT local que não é aceito pelo validador configurado para Auth0. Portanto, esse endpoint não é atualmente um fluxo de login utilizável.

## Modelo de dados

O diagrama a seguir apresenta o modelo entidade-relacionamento do projeto e os vínculos entre contas, chaves Pix, favorecidos, transferências e comprovantes.

![Diagrama entidade-relacionamento do Minpix](docs/MER.png)

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Web
- Spring Data JPA e Hibernate
- PostgreSQL (banco configurado para execução)
- H2 (dependência de runtime, não selecionada pela configuração atual)
- Springdoc OpenAPI
- Jakarta Bean Validation (dependência incluída; validações de entrada ainda não estão aplicadas)
- Lombok

## Configuração do banco de dados

A configuração em [`application.properties`](src/main/resources/application.properties) aponta para um PostgreSQL local, na porta `5432`, com o banco `banco_contas`. Antes de executar a aplicação ou os testes, inicie o PostgreSQL, crie esse banco e configure credenciais válidas no arquivo de propriedades. Não publique senhas ou outras credenciais no repositório.

O Hibernate está configurado com `ddl-auto=update`, que atualiza o esquema das tabelas durante a inicialização. O SQL também é exibido no log. Essa configuração é conveniente durante o desenvolvimento, mas deve ser revista antes de qualquer implantação.

## Estrutura do projeto

- `controllers`: endpoints REST.
- `models`: entidades JPA (`Conta`, `ChavePix`, `Favorecido`, `TransferenciaPix` e `Comprovante`).
- `repositories`: interfaces Spring Data JPA para persistência.
- `services`: operações de acesso e manutenção das entidades.
- `src/main/resources`: configuração da aplicação.
- `src/test`: testes automatizados.
- `MinpixApplication`: ponto de entrada Spring Boot.

## Pré-requisitos

- JDK 21 ou superior.
- PostgreSQL local configurado conforme a seção anterior.
- Maven instalado, ou use o Maven Wrapper incluído no projeto.

## Executar

Na raiz do projeto:

```bash
./mvnw spring-boot:run
```

Ou, com Maven instalado:

```bash
mvn spring-boot:run
```

A aplicação usa a porta padrão `8080`, salvo configuração diferente.

## Testes

Execute os testes com:

```bash
./mvnw test
```

O teste existente verifica se o contexto Spring é carregado. Como a configuração de datasource aponta para PostgreSQL, o banco e as credenciais precisam estar disponíveis também durante a execução dos testes.

## Limitações e próximos passos

- Validar o audience dos tokens Auth0 e definir autorização por escopos/recursos.
- Remover ou integrar corretamente o endpoint de login local ao fluxo de autenticação escolhido.
- Criar DTOs e validação de entrada para não expor as entidades diretamente na API.
- Implementar as regras de negócio da transferência Pix, incluindo saldo, limites, agendamento e atualização de status.
- Externalizar as configurações e credenciais do banco e definir uma estratégia de migração do esquema.
- Ampliar a cobertura de testes para os endpoints, validações e regras de negócio.
