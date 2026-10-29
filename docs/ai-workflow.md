# AI-Assisted Development Workflow

The assessment requires intentional use of AI while preserving engineering judgment and correctness. This log records where AI contributed and how its output was checked. It will be updated as the project evolves.

## Working agreement

- Use AI to inspect the repository, break work into reviewable increments, draft alternatives, implement scoped changes, and identify edge cases.
- Keep each change small enough to review and verify before committing it.
- Treat generated code and documentation as proposals, not authority.
- Verify changes with compilation, automated tests, diffs, and, where relevant, database constraints or runtime behavior.
- Record important assumptions and trade-offs in repository documentation rather than leaving them only in a chat transcript.
- Never include secrets, private employee information, or production data in prompts.

## Activity log

### Phase 1: framing and foundation

AI was used to:

- Extract and analyze the supplied assessment document.
- Inventory the generated Spring Boot project and existing design notes.
- Compare the assessment expectations with the proposed product scope.
- Propose a vertical-slice implementation and commit plan.
- Refine requirements, architecture, data-model, testing, and performance decisions.

Human/project decisions retained in the artifacts include PostgreSQL, React with TypeScript, a modular monolith, append-only salary revisions, UTC date validation, and deliberate scope exclusions.

Verification performed:

- Located IntelliJ's configured Microsoft OpenJDK 21 rather than changing the project's Java version.
- Ran the generated Spring Boot test successfully with Gradle 9.8.2.
- Reviewed staged diffs and created separate commits for the scaffold, requirements, and engineering decisions.

### Phase 2: persistence foundation

AI was used to:

- Add a reproducible local PostgreSQL service and environment-based application configuration.
- Check the Spring Boot 4.1 dependency catalog before selecting its split JPA/Flyway test starters and Testcontainers 2 modules.
- Translate the reviewed data model into a Flyway migration, JPA entities, repositories, and PostgreSQL integration tests.
- Review database constraints, query indexes, entity mappings, and repository method semantics.

Verification performed:

- Resolved dependencies and compiled production and test sources with Java 21.
- Ran a clean Gradle test build and confirmed that PostgreSQL tests are explicitly skipped when Docker is unavailable rather than run against a substitute database.
- Initially confirmed that no local PostgreSQL server was available, so database tests were transparently skipped.
- After Docker became available, ran the full suite against PostgreSQL. Hibernate validation exposed fixed-length country and currency columns mapped as variable-length strings; explicit JDBC `CHAR` mappings resolved the mismatch, after which all migration, repository, constraint, and context tests passed.

## Log template for later phases

For each material AI-assisted change, append:

1. The goal and constraints supplied to the AI.
2. The alternatives or risks considered.
3. The important decisions accepted or rejected.
4. The commands, tests, or manual checks used to verify the result.
