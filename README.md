
# **Sistema de Votação - Sicredi**

## **Descrição do Projeto**

Este é um sistema de votação desenvolvido em **Spring Boot 3** com uma arquitetura orientada a serviços. A aplicação gerencia pautas, sessões de votação e votos associados, garantindo que as regras de negócio sejam respeitadas. O projeto inclui a integração com APIs externas, filas do RabbitMQ e segue princípios **SOLID** e boas práticas de **Clean Code**.

---

## **Funcionalidades**

1. **Gerenciamento de Associados**
    - Cadastro automático de associados com dados fictícios.
    - Validação de CPF utilizando APIs externas.

2. **Gestão de Pautas**
    - Cadastro de novas pautas com validação dos dados de entrada.

3. **Sessões de Votação**
    - Criação de sessões de votação associadas a pautas.
    - Validação para impedir pautas duplicadas em sessões abertas.

4. **Registro de Votos**
    - Associados podem registrar votos (SIM/NAO) em sessões abertas.
    - Validação de votos duplicados e CPF habilitado para votar.

5. **Encerramento Automático de Sessões**
    - Utiliza **RabbitMQ** para publicar mensagens e encerrar sessões automaticamente após o tempo configurado.

6. **Documentação com Swagger**
    - Disponibiliza a documentação completa da API no formato OpenAPI 3.0.

---

## **Tecnologias Utilizadas**

- **Back-end**:
    - Java 17
    - Spring Boot 3.4.0
    - Spring Data JPA
    - Spring Web e WebFlux (WebClient)
    - Spring Validation
    - Spring AMQP (RabbitMQ)
    - Flyway (Migrações de Banco de Dados)
- **Banco de Dados**:
    - PostgreSQL
- **Ferramentas e Bibliotecas**:
    - Lombok
    - Swagger (springdoc-openapi 2.7.0)
    - Mockito e JUnit 5 (Testes Unitários)
    - RabbitMQ (Mensageria)
- **Infraestrutura**:
    - Docker (para RabbitMQ e PostgreSQL)

---

## **Requisitos**

- **JDK 17** ou superior
- **Maven** 3.8.5+
- **Docker** (para banco de dados e RabbitMQ)

---

## **Configuração do Banco de Dados e do RabbitMQ**

1. Suba um container via Docker:
   ```bash
   docker-compose up -d
   ```

2. Credenciais padrão:
    - **Banco**: votacao
    - **Usuário**: admin
    - **Senha**: admin


3. Acesse o painel de gerenciamento do RabbitMQ:
    - URL: [http://localhost:15672](http://localhost:15672)
    - Usuário: **guest**
    - Senha: **guest**

---

## **Execução da Aplicação**

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/sistema-votacao-sicredi.git
   cd sistema-votacao-sicredi
   ```

2. Compile o projeto:
   ```bash
   mvn clean install
   ```

3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse o Swagger para visualizar os endpoints:
    - URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## **Endpoints Principais**

### **Associados**
| Método | Endpoint                 | Descrição                        |
|--------|--------------------------|----------------------------------|
| POST   | `/api/v1/associados`     | Gera um novo associado.          |

### **Pautas**
| Método | Endpoint                 | Descrição                        |
|--------|--------------------------|----------------------------------|
| POST   | `/api/v1/pautas`         | Cria uma nova pauta.             |

### **Sessões**
| Método | Endpoint                 | Descrição                        |
|--------|--------------------------|----------------------------------|
| POST   | `/api/v1/sessoes`        | Cria uma nova sessão de votação. |

### **Votação**
| Método | Endpoint                          | Descrição                        |
|--------|-----------------------------------|----------------------------------|
| POST   | `/api/v1/votacao/sessao/{id}/voto` | Registra um voto na sessão.      |

---

## **Testes**

- **Testes Unitários**: Implementados com **JUnit 5** e **Mockito**.
- **Execução dos Testes**:
   ```bash
   mvn test
   ```

---

## **Princípios Seguidos**

1. **SOLID**:
    - Código modular, com separação de responsabilidades.
    - Validações e regras de negócio bem organizadas em métodos específicos.

2. **Clean Code**:
    - Métodos pequenos e coesos.
    - Nomes claros e semânticos para classes, métodos e variáveis.

3. **Boas Práticas**:
    - Tratamento de exceções robusto.
    - Logs informativos.
    - Testes unitários cobrindo os principais fluxos.

---

## **Autor**

Desenvolvido por **[AMANDA AZEVEDO]**.

📧 **Contato**: amaandaazevedo081@gmail.com 
🔗 **LinkedIn**: [linkedin.com/in/amandaazevedo-dev](https://www.linkedin.com/in/amandaazevedo-dev/)

---
