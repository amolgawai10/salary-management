package com.acme.salary_management.seed;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeSeedRecord(
        String employeeCode,
        String fullName,
        String department,
        String workCountry,
        boolean active,
        BigDecimal annualBaseAmount,
        String currencyCode,
        LocalDate effectiveDate,
        String reason
) {
}
