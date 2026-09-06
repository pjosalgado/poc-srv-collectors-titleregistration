# poc-srv-collectors-titleregistration

Collector's Channel Title Registration API — register and manage physical media titles (VHS, DVD, Blu-ray).

## Tech Stack

- Java 21 / Spring Boot 4.1.1
- MongoDB
- OpenAPI code-generated models (springdoc)
- MapStruct + Lombok
- Spock Framework (tests)

## Architecture

Clean Architecture with SOLID and CQRS principles.

- **entrypoint** — REST controllers, mappers, utilities
- **core** — use cases, boundaries (ports), domain models
- **dataprovider** — gateway implementations, entities, repository mappers

## API Endpoints

| Method   | Path                           | Description              |
|----------|--------------------------------|--------------------------|
| `GET`    | `/registration/v1/titles`      | List titles (paginated)  |
| `POST`   | `/registration/v1/titles`      | Register a title         |
| `GET`    | `/registration/v1/titles/{id}` | Get title by ID          |
| `PATCH`  | `/registration/v1/titles/{id}` | Update title by ID       |
| `DELETE` | `/registration/v1/titles/{id}` | Delete title by ID       |

## Running

```bash
./mvnw spring-boot:run              # port 8081
docker compose up -d --build        # app + MongoDB
```

## Testing

```bash
./mvnw test                         # Spock specs
```

## API Docs

Open http://localhost:8081/swagger-ui.html after starting the app.
