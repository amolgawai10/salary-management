package com.acme.salary_management.seed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class EmployeeSeedGeneratorTests {

    private final EmployeeSeedGenerator generator = new EmployeeSeedGenerator();

    @Test
    void generatesTenThousandDeterministicEmployees() {
        List<EmployeeSeedRecord> firstRun = generator.generate();
        List<EmployeeSeedRecord> secondRun = generator.generate();

        assertThat(firstRun)
                .hasSize(10_000)
                .isEqualTo(secondRun);
        assertThat(firstRun)
                .extracting(EmployeeSeedRecord::employeeCode)
                .doesNotHaveDuplicates()
                .startsWith("ACME-00001")
                .endsWith("ACME-10000");
    }

    @Test
    void generatesValidAndVariedCompensationData() {
        List<EmployeeSeedRecord> employees = generator.generate();

        assertThat(employees).allSatisfy(employee -> {
            assertThat(employee.workCountry()).matches("[A-Z]{2}");
            assertThat(employee.currencyCode()).matches("[A-Z]{3}");
            assertThat(employee.annualBaseAmount()).isPositive();
            assertThat(employee.annualBaseAmount().scale()).isEqualTo(4);
            assertThat(employee.effectiveDate()).isBeforeOrEqualTo(LocalDate.of(2025, 1, 1));
        });
        assertThat(employees).extracting(EmployeeSeedRecord::department)
                .contains("Engineering", "Finance", "Human Resources", "Legal", "Marketing",
                        "Operations", "Product", "Sales", "Security", "Support");
        assertThat(employees).extracting(EmployeeSeedRecord::workCountry)
                .contains("AU", "CA", "DE", "FR", "GB", "IN", "SG", "US");
        assertThat(employees).extracting(EmployeeSeedRecord::currencyCode)
                .contains("AUD", "CAD", "EUR", "GBP", "INR", "SGD", "USD");
    }

    @Test
    void rejectsNegativeEmployeeCount() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> generator.generate(-1))
                .withMessage("employeeCount must not be negative");
    }
}
