package com.acme.salary_management.seed;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("seed")
public class EmployeeSeedService {

    private static final int BATCH_SIZE = 1_000;

    private static final String INSERT_EMPLOYEE_SQL = """
            insert into employee (
                employee_code,
                full_name,
                department,
                work_country,
                active,
                version
            ) values (?, ?, ?, ?, ?, 0)
            on conflict (employee_code) do nothing
            """;

    private static final String INSERT_INITIAL_SALARY_SQL = """
            insert into salary_revision (
                employee_id,
                annual_base_amount,
                currency_code,
                effective_date,
                reason
            )
            select employee.id, ?, ?, ?, ?
            from employee
            where employee.employee_code = ?
            on conflict (employee_id, effective_date) do nothing
            """;

    private static final String SEEDED_EMPLOYEE_COUNT_SQL = """
            select count(*)
            from employee
            where employee_code like 'ACME-%'
            """;

    private static final String SEEDED_SALARY_COUNT_SQL = """
            select count(*)
            from salary_revision revision
            join employee on employee.id = revision.employee_id
            where employee.employee_code like 'ACME-%'
            """;

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeSeedGenerator generator;

    public EmployeeSeedService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.generator = new EmployeeSeedGenerator();
    }

    @Transactional
    public SeedResult seed() {
        List<EmployeeSeedRecord> employees = generator.generate();
        long employeesBefore = seededEmployeeCount();
        long salariesBefore = seededSalaryCount();

        jdbcTemplate.batchUpdate(
                INSERT_EMPLOYEE_SQL,
                employees,
                BATCH_SIZE,
                EmployeeSeedService::setEmployeeParameters
        );
        jdbcTemplate.batchUpdate(
                INSERT_INITIAL_SALARY_SQL,
                employees,
                BATCH_SIZE,
                EmployeeSeedService::setSalaryParameters
        );

        return new SeedResult(
                employees.size(),
                employeesBefore,
                seededEmployeeCount(),
                salariesBefore,
                seededSalaryCount()
        );
    }

    private long seededEmployeeCount() {
        return jdbcTemplate.queryForObject(SEEDED_EMPLOYEE_COUNT_SQL, Long.class);
    }

    private long seededSalaryCount() {
        return jdbcTemplate.queryForObject(SEEDED_SALARY_COUNT_SQL, Long.class);
    }

    private static void setEmployeeParameters(
            PreparedStatement statement,
            EmployeeSeedRecord employee
    ) throws SQLException {
        statement.setString(1, employee.employeeCode());
        statement.setString(2, employee.fullName());
        statement.setString(3, employee.department());
        statement.setString(4, employee.workCountry());
        statement.setBoolean(5, employee.active());
    }

    private static void setSalaryParameters(
            PreparedStatement statement,
            EmployeeSeedRecord employee
    ) throws SQLException {
        statement.setBigDecimal(1, employee.annualBaseAmount());
        statement.setString(2, employee.currencyCode());
        statement.setObject(3, employee.effectiveDate());
        statement.setString(4, employee.reason());
        statement.setString(5, employee.employeeCode());
    }

    public record SeedResult(
            int generatedEmployees,
            long employeesBefore,
            long employeesAfter,
            long salariesBefore,
            long salariesAfter
    ) {
    }
}
