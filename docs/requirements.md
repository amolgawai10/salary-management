# Salary Management Requirements

## Goal and user

Enable ACME's HR manager to manage annual base-salary information for 10,000 employees across multiple countries and answer common compensation questions without relying on spreadsheets.

## Scope and features

- Search employees by name or employee code and filter by country, department, and active status using server-side pagination.
- View an employee's details, current salary, currency, and complete salary revision history.
- Record an append-only salary revision with an effective date and reason. Backdated revisions are allowed; future-dated revisions are rejected.
- Report employee count, minimum, maximum, average, and total annual base salary by country and department.
- Keep every monetary calculation grouped by currency; never add or average unlike currencies.
- Reproducibly seed 10,000 synthetic employees without creating duplicates when the seed is rerun.

## Business rules

- Salary means annual gross base pay and must be greater than zero.
- Every salary has an explicit uppercase three-letter currency code independent of the employee's work country.
- An employee has at most one salary revision for a given effective date.
- Current salary is the revision with the latest effective date; revision rows are not edited or deleted through the application.
- Reports cover all employees matching the selected criteria, not only employees on the visible page.
- The backend uses UTC when deciding whether an effective date is in the future.
- Concurrent salary changes based on stale employee data are rejected rather than silently accepted.

## Deliberate exclusions

- **Employee lifecycle management:** employee creation, editing, and deletion are not needed to demonstrate the salary-management workflow; employees are supplied by the deterministic seed.
- **Authentication and authorization:** the assessment has one HR-manager persona and does not define an identity provider or role model.
- **Payroll, taxes, and payments:** these require financial, regulatory, and banking capabilities beyond salary-information management.
- **Bonuses and benefits:** the first release is limited to annual gross base pay.
- **Currency conversion:** exchange-rate sourcing and conversion policy are undefined, so reports retain their original currencies.
- **Spreadsheet import/export:** this is not required for the core employee and salary workflow.
- **Approval and correction workflows:** the first release records valid revisions but does not model approvals or the audited correction of erroneous history.

## Technical approach

- Java 21 and Spring Boot backend with a PostgreSQL relational database.
- React and TypeScript frontend. This is an explicit project choice; modern Angular would be the alternative if the role being assessed specifically requires it.
- REST API, database migrations, deterministic automated tests, and production deployment configuration.

## Acceptance criteria

- HR can find an employee and inspect their details, current salary, and revision history.
- HR can save a valid salary revision and immediately see it reflected in the employee view and reports.
- Invalid, future-dated, duplicate-date, or conflicting salary changes receive a clear error and do not partially update data.
- Reports calculate across all matching employees and never combine different currencies.
- Persisted data survives an application restart.
- Rerunning the seed leaves exactly 10,000 uniquely identified employees, each with an initial salary.

## Assessment deliverables

- Fast, deterministic tests covering the core business behavior.
- Incremental Git commits that keep the project in a working state.
- Requirements, architecture, trade-off, AI-workflow, and performance notes.
- A deployed application, short video demonstration, and repository link.

## Assumptions to revisit if clarified

The assessment leaves some product details open. PostgreSQL, React, UTC, supported workflows, and the exclusion of employee CRUD and authentication are project decisions made to keep the solution focused. They should be revisited if the hiring team provides different expectations.
