# Product Sorter (ITX Backend Tools Practical Case)

REST service that sorts a list of T-shirts by combining multiple sorting criteria, each one with its own weight. A product's final score is the weighted sum of all criteria.

Included criteria:

- `sales_units`: scores each product based on units sold, normalized against the top seller in the catalog.
- `stock_ratio`: scores each product by the proportion of sizes with available stock.

The design is meant to support new criteria without modifying existing code (Strategy + automatic registration through Spring).

## Stack

- Java 21, Spring Boot 3.3
- Spring Data MongoDB
- JUnit 5, AssertJ, Mockito
- Testcontainers (Mongo) for integration and E2E
- REST Assured for E2E
- PIT (pitest) for mutation testing

## Architecture

Single bounded context: `products`, split into three layers following hexagonal architecture:

```
products
├── domain
│   └── product
│       ├── interfaces    `ProductRepositoryInterface`, `SortingCriterionInterface`, `ScorerInterface`
│       ├── sorting       Criteria implementations, catalog, scoring service, `Score`/`Weight`/`CriterionWeights`
│       └── valueObjects  `ProductId`, `ProductName`, `SalesUnits`, `StockBySize`, ...
├── application
│   └── `SortProductsInput` / `SortProductsUseCase` / `SortedProductsResponse`
└── infrastructure
    ├── persistence  Mongo adapter for the port + ProductDocument
    ├── http         Controller, HTTP request/response DTOs, exception handler
    └── bootstrap    Bean configuration and catalog seeder
```

Dependencies always point inward. The domain does not know about Spring, Mongo, or HTTP. The Mongo adapter implements the repository port. The HTTP layer translates between HTTP and `SortProductsInput`.

### Why there is no transactional messaging

This component performs weighted reads over a persisted catalog. It does not emit domain state changes and does not need to coordinate consistency between local writes and event publication. Adding a transactional outbox would introduce a pattern without a problem to solve, complicate deployment (broker, relay), and force artificial events. It is intentionally omitted; if write use cases with external side effects appear in the future (for example, propagating stock changes), it should then be introduced.

### About normalization

To make weights meaningful when criteria produce values on different scales (sales units vs ratio in `[0, 1]`), each criterion normalizes its output to the `[0, 1]` range:

- `SalesUnitsCriterion` divides each product's sales by the maximum sales value in the catalog.
- `StockRatioCriterion` already produces a `[0, 1]` ratio by construction.

This means a combination such as `sales_units=0.5, stock_ratio=0.5` balances both criteria comparably.

## Endpoint

`POST /products/sorted`

Request body:

```json
{
  "weights": {
    "sales_units": 0.8,
    "stock_ratio": 0.2
  }
}
```

`200 OK` response:

```json
{
  "items": [
    { "id": 5, "name": "CONTRASTING LACE T-SHIRT", "score": 0.8666666... },
    { "id": 3, "name": "RAISED PRINT T-SHIRT",     "score": 0.2984615... }
  ]
}
```

`400 Bad Request` errors:

| `code`                       | When                                                                    |
|------------------------------|-------------------------------------------------------------------------|
| `validation_failed`          | The body does not satisfy declarative constraints (empty, null, ...)   |
| `invalid_request`            | Weights are out of range, all are zero, etc.                           |
| `unknown_sorting_criterion`  | The body references a criterion that is not registered in the service   |
| `malformed_request`          | The body is not valid JSON                                              |

## How to run it

### With Docker (recommended)

```
docker compose up --build
```

This starts MongoDB and the application. The seeder inserts the sample catalog if the collection is empty. Endpoint available at `http://localhost:8080/products/sorted`.

Request example:

```
curl -X POST http://localhost:8080/products/sorted \
  -H 'Content-Type: application/json' \
  -d '{"weights":{"sales_units":0.8,"stock_ratio":0.2}}'
```

### With local Maven

If you have Java 21 and Maven installed:

```
mvn spring-boot:run
```

It requires Mongo accessible at `mongodb://localhost:27017/product_sorter`. You can start only the Mongo service with:

```
docker compose up mongo
```

### Without Java or Maven installed

You can run Maven using the official container:

```
docker run --rm -it -v "$PWD":/workspace -w /workspace \
  maven:3.9-eclipse-temurin-21 mvn test
```

## Tests

Three test types, separated by naming convention:

- Unit (`*Test`): domain, criteria, scoring service, use case. No Spring, no Mongo.
- Integration (`*IntegrationTest`): Mongo adapter with Testcontainers.
- E2E (`*E2ETest`): full Spring Boot context + Mongo Testcontainers + REST Assured against the real endpoint.

Commands:

```
mvn test           # unit only
mvn verify         # unit + integration + E2E
```

Requirement for integration and E2E: Docker running on the machine (required by Testcontainers).

## Mutation testing

PIT is configured to mutate domain and application layers. Integration and E2E tests are excluded so the analysis stays fast and focused on business logic.

```
mvn test verify org.pitest:pitest-maven:mutationCoverage
```

HTML report at `target/pit-reports/index.html`. Current thresholds: 75% mutation score, 80% line coverage.

## Extend with a new criterion

1. Create a class in `products.domain.product.sorting` that implements `SortingCriterionInterface`, with its own `SortingCriterionId`.
2. Register it as a `@Bean` (or add it to `SortingConfiguration`). Spring will inject it automatically into the criteria catalog.
3. Add unit tests for the new criterion and extra cases in `ProductScoringServiceTest`.

No controller or use case changes are required.
