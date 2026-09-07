# poc-srv-collectors-titleregistration

Collector's Channel Title Registration API — register and manage physical media titles (VHS, DVD, Blu-ray).

## Tech Stack

- Java 21 / Spring Boot 4.1.1
- MongoDB
- RabbitMQ
- OpenAPI code-generated models (springdoc)
- MapStruct + Lombok
- Spock Framework (tests)

## Architecture

Clean Architecture with SOLID and CQRS principles.

- **entrypoint** — REST controllers, mappers, utilities
- **core** — use cases, boundaries (ports), domain models
- **dataprovider** — gateway implementations (`db/`, `messaging/`)

### Design Patterns

- **Tiny Parameters** — avoid more than one parameter per method; wrap in records

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
docker compose up -d --build        # app + MongoDB + RabbitMQ
```

## Testing

```bash
./mvnw test                         # Spock specs
```

## Docker Images

Available on both Docker Hub and GitHub Packages (multi-platform: `linux/amd64`, `linux/arm64`):

**Docker Hub:**
```bash
docker pull paulosalgado/collectors-titleregistration:latest
docker pull paulosalgado/collectors-titleregistration:<version>
```

**GitHub Packages (ghcr.io):**
```bash
docker pull ghcr.io/pjosalgado/collectors-titleregistration:latest
docker pull ghcr.io/pjosalgado/collectors-titleregistration:<version>
```

## API Docs

Open http://localhost:8081/swagger-ui.html after starting the app.

## RabbitMQ

Management UI at http://localhost:15672.
