<!-- bmad:context -->
## poc-srv-collectors-titleregistration

Collector's Channel Title Registration API.

Java 21 / Spring Boot 4.1.1, MongoDB, RabbitMQ, Redis, OpenAPI and AsyncAPI code-generated models.

## Architecture

Clean Architecture + SOLID + CQRS.

Layers: `entrypoint` → `core` → `dataprovider`.

- **entrypoint** — REST controllers, mappers, utilities (no business logic)
- **core** — use cases, boundaries (ports), domain models (no framework deps)
- **dataprovider** — gateway implementations; sub-packages: `db/` (entities, repositories, mappers), `messaging/` (event publishers), `cache/` (Redis cache), `rest/` (external API clients)

## Key patterns

- **One use case per controller operation** — use case owns find, validate, transform, persist
- **Domain models control state** — no `@Setter`; use `applyUpdates(Title)` for encapsulation; `clearEnrichmentData()` for re-enrichment triggers
- **Domain exceptions** thrown by use cases, mapped to HTTP by `@RestControllerAdvice` handlers
- **All mappers use MapStruct** with `componentModel = "spring"`
- **Date conversion** — `DateMappingUtils.toOffsetDateTime()` converts `LocalDateTime` → `OffsetDateTime` for API responses
- **MongoDB auditing** — entities implement `Persistable<String>` with `isNew()` returning `createdDateTime == null` to enable `@CreatedDate`/`@LastModifiedDate`
- **Request/Response wrappers** — requests: `{"data": ...}`, responses: `{"data": ..., "_links": ...}`
- **HATEOAS** — responses include `_links` with relative paths
- **Error responses** — `{"errors": [{"code": "...", "title": "...", "detail": "..."}]}`
- **Async events** — use cases publish to RabbitMQ via boundary interfaces after persist (e.g. `title.enrichment` queue)
- **External services via boundaries** — all external service access (persistence, messaging, caching, third-party APIs) must go through boundary interfaces in `core/boundary/`; implementations live in `dataprovider/`
- **External API clients** — use Spring Boot's `RestClient` for HTTP calls (e.g. `OmdbClient`)
- **Caching** — Redis with configurable TTL via `@ConfigurationProperties`

## Conventions

- **Core must not import Spring, MongoDB, or dataprovider packages** — use cases depend only on boundary (port) interfaces; dataprovider implements them
- **Dependency inversion** — `core/boundary/` defines outbound ports (e.g. `TitlePersistenceBoundary`, `TitleEventsBoundary`, `TitleCacheBoundary`, `OmdbBoundary`); `dataprovider/` provides the adapters
- **Tiny parameters** — avoid more than one parameter per method; especially native Java types. Wrap parameters in records (e.g. `TitleEnrichmentRequest`, `TitleUpdateContext`, `PageLinksContext`)
- Controller builds domain models from requests — use cases never see OpenAPI types
- Boundary methods: `create`/`findById`/`findAll`/`update`/`deleteById` (persistence), `publishTitleEnrichment` (events), `get`/`put` (cache), `searchByTitle` (OMDb)
- Externalized config via `@ConfigurationProperties` classes in `config.properties` package
- Utility classes use Lombok `@UtilityClass`
- Constructor pattern: Lombok `@Builder`

## Running

```bash
./mvnw spring-boot:run          # port 8081
./mvnw test                     # Spock specs
docker compose up -d --build    # app + MongoDB + RabbitMQ + Redis
```

## Testing

All tests use **Spock Framework** (Groovy) in `src/test/groovy/`.

- **Unit** — pure logic, no Spring context. Mock with Spock stubs/mocks.
- **Integration** — single layer against real infrastructure. MongoDB tests `@Ignore`d; run with local MongoDB.
- **Contract** — validate request/response shapes against OpenAPI contract.
- **Component** — full-stack entrypoint → core → dataprovider.

Run: `./mvnw test` (unit tests only). MongoDB tests require local instance on `localhost:27017`.

## Pitfalls

- OpenAPI codegen overwrites `openapi/**` on every build — edit `openapi-contract.yaml` only
- AsyncAPI codegen generates models in `target/generated-sources/asyncapi/` — edit `asyncapi-contract.yaml` only
- MapStruct processor must run after Lombok — `pom.xml` orders them explicitly
- Spring Data Redis 4.0+ — use `RedisSerializer.json()` instead of deprecated `GenericJackson2JsonRedisSerializer`
<!-- /bmad:context -->
