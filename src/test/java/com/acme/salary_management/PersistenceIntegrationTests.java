package com.acme.salary_management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.acme.salary_management.employee.Employee;
import com.acme.salary_management.employee.EmployeeRepository;
import com.acme.salary_management.salary.SalaryRevision;
import com.acme.salary_management.salary.SalaryRevisionRepository;
import com.acme.salary_management.support.PostgreSqlIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PersistenceIntegrationTests extends PostgreSqlIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRevisionRepository salaryRevisionRepository;

    @Test
    void persistsEmployeeAndFindsLatestSalaryRevision() {
        Employee employee = employeeRepository.saveAndFlush(new Employee(
                "ACME-00001",
                "Asha Patel",
                "Engineering",
                "IN",
                true
        ));

        SalaryRevision initialSalary = salaryRevisionRepository.saveAndFlush(new SalaryRevision(
                employee,
                new BigDecimal("1250000.0000"),
                "INR",
                LocalDate.of(2024, 4, 1),
                "Initial salary"
        ));
        SalaryRevision currentSalary = salaryRevisionRepository.saveAndFlush(new SalaryRevision(
                employee,
                new BigDecimal("1400000.0000"),
                "INR",
                LocalDate.of(2025, 4, 1),
                "Annual review"
        ));

        assertThat(employeeRepository.findByEmployeeCode("ACME-00001"))
                .contains(employee);
        assertThat(salaryRevisionRepository.findFirstByEmployeeIdOrderByEffectiveDateDesc(employee.getId()))
                .contains(currentSalary);
        assertThat(salaryRevisionRepository.findByEmployeeIdOrderByEffectiveDateDesc(employee.getId()))
                .containsExactly(currentSalary, initialSalary);
        assertThat(initialSalary.getRecordedAt()).isNotNull();
    }

    @Test
    void databaseRejectsTwoRevisionsForSameEmployeeAndEffectiveDate() {
        Employee employee = employeeRepository.saveAndFlush(new Employee(
                "ACME-00002",
                "Noah Williams",
                "Finance",
                "GB",
                true
        ));
        LocalDate effectiveDate = LocalDate.of(2025, 7, 1);

        salaryRevisionRepository.saveAndFlush(new SalaryRevision(
                employee,
                new BigDecimal("80000.0000"),
                "GBP",
                effectiveDate,
                "Initial salary"
        ));

        assertThatThrownBy(() -> salaryRevisionRepository.saveAndFlush(new SalaryRevision(
                employee,
                new BigDecimal("85000.0000"),
                "GBP",
                effectiveDate,
                "Duplicate effective date"
        ))).isInstanceOf(DataIntegrityViolationException.class);
    }
}
