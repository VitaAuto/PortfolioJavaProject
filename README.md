# Portfolio_Java_Project
## Overview
This is a multi-module Java application, featuring a RESTful API and automated tests.
The project includes:

### API module: 
- Spring Boot REST API controller
- Tests module: Automated tests with Allure reporting
- PostgreSQL: For data storage
- Docker & Docker Compose: For easy local deployment

#### Technologies Used
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Lombok
- JUnit 5
- Cucumber
- RestAssured
- WireMock
- Allure
- Docker, Docker Compose
- OpenAPI (Swagger UI)

#### Getting Started
1. Clone the repository: 
git clone https://gitlab.com/vitalitalipski1/portfolio_java_project.git
2. Start with Docker Compose to activate the database and api-controller (or use @mock tag for running tests with prepared stubs and without starting real database and api-controller):

docker-compose up --build
- The API will be available at: http://localhost:8082
- PostgreSQL will be available on port: 5454
3. API Documentation (Swagger UI)
After starting the API, documentation is available at: http://localhost:8082/swagger-ui.html

#### API Endpoints
- GET /api/orders — Get all orders
- GET /api/orders/{id} — Get order by ID
- POST /api/orders — Create a new order
- PUT /api/orders/{id} — Update an order completely
- PATCH /api/orders/{id} — Partially update an order
- DELETE /api/orders/{id} — Mark order as deleted
- DELETE /api/orders/hard/{id} — Physically delete order

#### Database
- Uses PostgreSQL (via Docker Compose)
- Initialization script: db-init/init.sql
Data is persisted in the pgdata Docker volume

#### Build and Run Manually
#### Build API

cd api

./gradlew build

#### Run API
java -jar build/libs/api.jar

### Automated Testing module:
The tests module contains automated integration and API tests for the project.

#### Key features:

- BDD-style scenarios using Cucumber (feature files in src/test/resources/features)
- JUnit 5 as the test runner
- RestAssured for HTTP API testing
- WireMock for mocking external dependencies (scenarios with @mock tag)
- Allure for test reporting

#### Running Tests
./gradlew :tests:test - all tests
./gradlew :tests:smokeTest - only smoke tests

#### Generating Allure Report
After running tests, generate and open the Allure report (You need Allure CLI installed):
- allure generate tests/allure-results -o allure-report --clean
- allure serve tests/allure-results
- https://github.com/VitaAuto/PortfolioJavaProject - to see results on GitHub Pages

#### Example Feature
Feature files are written in Gherkin syntax.

Example: order.feature describes scenarios for getting, creating, updating, and deleting orders.

#### Environment Variables (API)
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SERVER_PORT

All variables are set in docker-compose.yml for the API container.