# Minpix

Minpix é uma API REST em Java com Spring Boot para gestão de contas, chaves Pix, favorecidos, transferências e comprovantes.

## Visão geral

O projeto implementa um backend bancário de exemplo com:

- CRUD completo para contas, chaves Pix, favorecidos, transferências e comprovantes.
- DTOs de entrada e saída para separar a camada HTTP da modelagem JPA.
- Validações com Bean Validation para CPF, CNPJ, tipo/valor de chave Pix, valores monetários e campos obrigatórios.
- Proteção de endpoints via Spring Security e OAuth2 Resource Server com JWT do Auth0.
- Documentação OpenAPI/Swagger para consulta e testes manuais.
- Testes de unidade, integração e validação de payloads.

A aplicação é um projeto em desenvolvimento e não deve ser usada em produção com dados financeiros reais.

## Status atual

### Funcionalidades implementadas

- Entidades JPA e repositórios Spring Data JPA para todas as principais estruturas do domínio.
- Services para criação, listagem, consulta, atualização e exclusão das entidades.
- Controllers REST com endpoints para `/contas`, `/chaves-pix`, `/favorecidos`, `/transferencias-pix` e `/comprovantes`.
- Tratamento centralizado de erros para recursos inexistentes, número de conta duplicado e validações de entrada.
- Redução de exposição de dados sensíveis: as respostas de conta não retornam a senha.
- Configuração de segurança exigindo autenticação JWT para todas as rotas, exceto documentação pública do Swagger/OpenAPI.
- Validação customizada de chaves Pix com correspondência entre tipo e valor.
- Domínio de status controlado pelo enum `StatusPixEnum` em [`src/main/java/com/example/minpix/enums`](src/main/java/com/example/minpix/enums), com valores pré-definidos e validação em runtime.
- Testes de integração cobrindo criação, consulta, atualização, exclusão e autenticação de endpoints.

### Limitações conhecidas

- As transferências Pix ainda são persistidas, mas não executam regras de negócio financeiras; elas não movimentam saldo, não validam limites nem processam agendamentos ou status automáticos.
- O audience do token Auth0 não é validado explicitamente pelo `JwtDecoder`.
- O endpoint `/auth/login` existe, mas não representa um fluxo de autenticação totalmente integrado ao Auth0 e continua sendo um ponto de atenção para a arquitetura de segurança.

## API REST

Todos os recursos suportam as operações padrão abaixo:

| Operação | Método e caminho | Resposta esperada |
|---|---|---|
| Listar | `GET /{recurso}` | `200 OK` com lista |
| Consultar por ID | `GET /{recurso}/{id}` | `200 OK` ou `404 Not Found` |
| Cadastrar | `POST /{recurso}` | `201 Created` |
| Atualizar | `PUT /{recurso}/{id}` | `200 OK` ou `404 Not Found` |
| Excluir | `DELETE /{recurso}/{id}` | `204 No Content` ou `404 Not Found` |

Recursos disponíveis:

| Recurso | Caminho |
|---|---|
| Contas | `/contas` |
| Chaves Pix | `/chaves-pix` |
| Favorecidos | `/favorecidos` |
| Transferências Pix | `/transferencias-pix` |
| Comprovantes | `/comprovantes` |

### Exemplos de payloads

Cadastro de conta:

```json
{
  "nomeTitular": "Maria Silva",
  "cpf": "529.982.247-25",
  "banco": "Banco XPTO",
  "agencia": "0001",
  "numeroConta": "12345-6",
  "senha": "senha-segura",
  "saldo": 1500.50,
  "limiteDiario": 1000.00
}
```

Chave Pix:

```json
{
  "tipo": "CPF",
  "valorChave": "529.982.247-25",
  "contaId": 1,
  "ativa": true
}
```

Favorecido:

```json
{
  "nome": "João Souza",
  "banco": "Banco XPTO",
  "chavePix": "joao@example.com",
  "tipoChave": "EMAIL",
  "contaOrigemId": 1
}
```

Transferência Pix:

```json
{
  "contaOrigemId": 1,
  "favorecidoId": 1,
  "valor": 250.00,
  "descricao": "Pagamento",
  "dataTransferencia": "2026-10-30",
  "dataAgendamento": null,
  "status": "PENDENTE"
}
```

Comprovante:

```json
{
  "transferenciaId": 1,
  "codigoAutenticacao": "ABC123XYZ987",
  "dataGeracao": "2026-10-30",
  "status": "GERADO"
}
```

### Regras de validação importantes

- CPF aceita com ou sem máscara, desde que tenha dígitos verificadores válidos.
- Chaves Pix aceitam `CPF`, `CNPJ`, `EMAIL`, `TELEFONE` e `ALEATORIA`.
- O valor da chave deve corresponder ao tipo informado.
- `valor`, `saldo` e `limiteDiario` devem ser positivos ou não negativos, conforme o caso, e respeitar a precisão monetária.
- `numeroConta` deve ser único; a duplicidade retorna `409 Conflict`.
- IDs de relacionamento devem existir; caso contrário, a API retorna `404 Not Found`.
- Campos obrigatórios ou inválidos geram `400 Bad Request` com erros por campo.

## Segurança e autenticação

A aplicação usa Spring Security com OAuth2 Resource Server e um `JwtDecoder` configurado com `issuer-uri` do Auth0.

Configuração atual:

- `GET /swagger-ui/**` e `GET /v3/api-docs/**` ficam liberados.
- Todos os demais endpoints exigem autenticação JWT.
- O token deve ser enviado no cabeçalho `Authorization` no formato `Bearer <token>`. 
- O `auth0.audience` foi configurado para uso em ambiente Auth0, mas o validador ainda não checa o audience do token de forma explícita.

O endpoint `/auth/login` existe no backend, mas ainda não é um fluxo de login confiável e completo para uso com Auth0; ele foi mantido apenas como ponto de integração inicial para autenticação local/experimental.

### Exemplo de teste no Postman

1. Registre uma API no Auth0 e configure o `Identifier` para corresponder ao valor de `auth0.audience` em [`application.properties`](src/main/resources/application.properties).
2. Crie uma aplicação do tipo **Machine to Machine** e conceda acesso à API.
3. Configure `spring.security.oauth2.resourceserver.jwt.issuer-uri` com o domínio do tenant Auth0, incluindo `https://` e a barra final.
4. Solicite um token via **Client Credentials** em `https://SEU-DOMINIO/oauth/token`.
5. Envie o `access_token` no cabeçalho `Authorization` como `Bearer <token>` em chamadas para `http://localhost:8080/contas`.

## Modelo de dados

![Diagrama entidade-relacionamento do Minpix](docs/MER.png)

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Web
- Spring Data JPA + Hibernate
- PostgreSQL (execução local)
- H2 (perfil de testes)
- Springdoc OpenAPI / Swagger UI
- Jakarta Bean Validation
- Lombok
- Spring Security + OAuth2 Resource Server

## Configuração do banco de dados

A aplicação principal usa PostgreSQL local em `localhost:5432`, banco `banco_contas`, conforme [`application.properties`](src/main/resources/application.properties).

Antes de executar a aplicação:

1. Inicie o PostgreSQL.
2. Crie o banco `banco_contas`.
3. Configure as credenciais do datasource no arquivo de propriedades.
4. Não versionar senhas e segredos no repositório.

O Hibernate usa `ddl-auto=update` em ambiente principal, e o SQL é exibido no log.

Para testes, o projeto usa `src/test/resources/application-test.properties`, com banco em memória H2 e `ddl-auto=create-drop`.


Com PostgreSQL local, execute:

```bash
psql -v ON_ERROR_STOP=1 -h localhost -p 5432 -U postgres -d banco_contas -f scripts/seed-data.sql
```

## Estrutura do projeto

- `controllers`: endpoints REST.
- `dto/request` e `dto/response`: contratos de entrada e saída da API.
- `enums`: enums de domínio, como `StatusPixEnum`.
- `validation`: validações customizadas de CPF e chaves Pix.
- `exceptions`: tratamento de erros de negócio e validação.
- `models`: entidades JPA (`Conta`, `ChavePix`, `Favorecido`, `TransferenciaPix` e `Comprovante`).
- `repositories`: interfaces Spring Data JPA.
- `services`: regras e operações de persistência.
- `config`: configuração de segurança, JWT e documentação.
- `src/main/resources`: configuração da aplicação.
- `src/test`: testes automatizados.
- `MinpixApplication`: ponto de entrada da aplicação.

## Pré-requisitos

- JDK 21+
- PostgreSQL local configurado para execução principal
- Maven ou Maven Wrapper

## Executar a aplicação

Na raiz do projeto:

```bash
./mvnw spring-boot:run
```

Ou:

```bash
mvn spring-boot:run
```

A aplicação usa a porta padrão `8080`.

## Documentação interativa

Após iniciar a aplicação:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes

Execute a suíte de testes com:

```bash
./mvnw test
```

Os testes cobrem:

- carregamento do contexto Spring,
- validações de DTO,
- regras de integração,
- ausência da senha nas respostas de conta,
- rejeição de número de conta duplicado,
- autenticação de endpoints protegidos.

## Próximos passos

- Validar o `audience` dos tokens Auth0 e definir autorização por escopo/recurso.
- Consolidar a autenticação em um fluxo único e seguro para login e acesso à API.
- Implementar regras reais de transferência Pix, incluindo saldo, limites, agendamento e atualização de status.
- Externalizar credenciais e configurar estratégias de segurança e migração de dados para ambientes de produção.
- Expandir cobertura de testes para regras de negócio e fluxos completos de integração.
