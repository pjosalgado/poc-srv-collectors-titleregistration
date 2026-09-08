# poc-srv-collectors-titleregistration

Collector's Channel Title Registration API — register and manage physical media titles (VHS, DVD, Blu-ray).

## Tech Stack

- **Core**: Java 21, Spring Boot 4.1.1
- **Data Stores**: MongoDB, Redis
- **Messaging**: RabbitMQ
- **API & Contracts**: OpenAPI, AsyncAPI, RestClient
- **Code Generation**: MapStruct, Lombok
- **Testing**: Spock Framework

## Architecture

Clean Architecture with SOLID and CQRS principles.

- **entrypoint** — REST controllers, mappers, utilities
- **core** — use cases, boundaries (ports), domain models
- **dataprovider** — gateway implementations (`db/`, `messaging/`, `cache/`, `rest/`)

### Patterns

- **Tiny Parameters** — avoid more than one parameter per method; wrap in records
- **Dependency Inversion** — all external service access goes through boundary interfaces in `core/boundary/`; implementations live in `dataprovider/`

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
docker compose up -d --build        # app + MongoDB + RabbitMQ + Redis
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | `mongodb://titleregistration:password@localhost:27017/titleregistration?authSource=admin` |
| `RABBITMQ_HOST` | RabbitMQ host | `localhost` |
| `RABBITMQ_PORT` | RabbitMQ port | `5672` |
| `RABBITMQ_USERNAME` | RabbitMQ username | `titleregistration` |
| `RABBITMQ_PASSWORD` | RabbitMQ password | `password` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `REDIS_PASSWORD` | Redis password | `password` |
| `OMDB_API_KEY` | OMDb API key | (required for enrichment) |

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

RabbitMQ Management UI — http://localhost:15672
