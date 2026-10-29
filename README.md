# Minpix

Minpix é um projeto Java em desenvolvimento para representar operações e dados relacionados a contas bancárias e ao Pix.

## Estado atual

O projeto possui entidades JPA para contas, chaves Pix, favorecidos, transferências e comprovantes, além de repositórios Spring Data JPA e serviços com operações básicas de cadastro, consulta, atualização e exclusão.

O `ContaService` também preenche a data de cadastro ao criar uma conta. Os serviços de transferência, chave Pix, favorecido e comprovante fazem operações de persistência; eles ainda não executam regras de negócio de uma transferência Pix.

**A API HTTP ainda não está implementada:** não há controllers nem rotas REST no código atual. Portanto, os serviços não podem ser chamados por endpoints como `GET /contas` ou `POST /contas`.

Também não estão configurados autenticação Auth0/JWT nem validações de regras como saldo positivo, limite diário ou obrigatoriedade de campos. Essas funcionalidades não devem ser consideradas disponíveis.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Data JPA
- H2 como dependência de runtime
- Springdoc OpenAPI como dependência
- Lombok

O arquivo `application.properties` configura apenas o nome da aplicação. Não há configuração de banco externo; com a dependência H2, a aplicação usa a configuração padrão para banco embarcado, cujos dados são voláteis. Embora Springdoc esteja incluído, ainda não há controllers com endpoints para documentar.

## Estrutura

- `models`: entidades JPA (`Conta`, `ChavePix`, `Favorecido`, `TransferenciaPix` e `Comprovante`).
- `repositories`: interfaces Spring Data JPA para persistência.
- `services`: operações de acesso e manutenção das entidades.
- `MinpixApplication`: ponto de entrada Spring Boot.

## Modelo de dados

O diagrama a seguir apresenta o modelo entidade-relacionamento do projeto e os vínculos entre contas, chaves Pix, favorecidos, transferências e comprovantes.

![Diagrama entidade-relacionamento do Minpix](docs/MER.png)

## Pré-requisitos

- JDK 21 ou superior.
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

O Spring Boot usa a porta padrão `8080`, salvo configuração diferente. Como não há controllers, iniciar a aplicação não disponibiliza atualmente endpoints de negócio.

## Testes

Execute os testes com:

```bash
./mvnw test
```

O teste existente verifica se o contexto Spring da aplicação é carregado.

## Próximos passos

Para disponibilizar uma API utilizável, será necessário criar controllers REST e definir contratos de requisição/resposta. Autenticação, validação, regras de transferência Pix e configuração de persistência adequada a cada ambiente também precisam ser implementadas antes de serem anunciadas como funcionalidades.
