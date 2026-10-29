package com.acme.salary_management.seed;

import static org.assertj.core.api.Assertions.assertThat;

import com.acme.salary_management.employee.Employee;
import com.acme.salary_management.employee.EmployeeRepository;
import com.acme.salary_management.salary.SalaryRevision;
import com.acme.salary_management.salary.SalaryRevisionRepository;
import com.acme.salary_management.support.PostgreSqlIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("seed")
class EmployeeSeedIntegrationTests extends PostgreSqlIntegrationTest {

    @Autowired
    private EmployeeSeedService seedService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRevisionRepository salaryRevisionRepository;

    @Test
    void seedsExpectedDataAndIsIdempotent() {
        assertThat(employeeRepository.count()).isEqualTo(10_000);
        assertThat(salaryRevisionRepository.count()).isEqualTo(10_000);

        EmployeeSeedRecord expected = new EmployeeSeedGenerator().generate(1).getFirst();
        Employee employee = employeeRepository.findByEmployeeCode(expected.employeeCode()).orElseThrow();
        SalaryRevision salary = salaryRevisionRepository
                .findFirstByEmployeeIdOrderByEffectiveDateDesc(employee.getId())
                .orElseThrow();

        assertThat(employee.getFullName()).isEqualTo(expected.fullName());
        assertThat(employee.getDepartment()).isEqualTo(expected.department());
        assertThat(employee.getWorkCountry()).isEqualTo(expected.workCountry());
        assertThat(employee.isActive()).isEqualTo(expected.active());
        assertThat(salary.getAnnualBaseAmount()).isEqualByComparingTo(expected.annualBaseAmount());
        assertThat(salary.getCurrencyCode()).isEqualTo(expected.currencyCode());
        assertThat(salary.getEffectiveDate()).isEqualTo(expected.effectiveDate());
        assertThat(salary.getReason()).isEqualTo(expected.reason());

        EmployeeSeedService.SeedResult secondRun = seedService.seed();

        assertThat(secondRun.employeesBefore()).isEqualTo(10_000);
        assertThat(secondRun.employeesAfter()).isEqualTo(10_000);
        assertThat(secondRun.salariesBefore()).isEqualTo(10_000);
        assertThat(secondRun.salariesAfter()).isEqualTo(10_000);
        assertThat(employeeRepository.count()).isEqualTo(10_000);
        assertThat(salaryRevisionRepository.count()).isEqualTo(10_000);
    }
}
