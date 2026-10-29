package com.acme.salary_management.salary;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.acme.salary_management.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "salary_revision")
public class SalaryRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    private Employee employee;

    @Column(name = "annual_base_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal annualBaseAmount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(nullable = false, length = 500)
    private String reason;

    @CreationTimestamp
    @Column(name = "recorded_at", nullable = false, updatable = false)
    private Instant recordedAt;

    protected SalaryRevision() {
    }

    public SalaryRevision(
            Employee employee,
            BigDecimal annualBaseAmount,
            String currencyCode,
            LocalDate effectiveDate,
            String reason
    ) {
        this.employee = employee;
        this.annualBaseAmount = annualBaseAmount;
        this.currencyCode = currencyCode;
        this.effectiveDate = effectiveDate;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public BigDecimal getAnnualBaseAmount() {
        return annualBaseAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public String getReason() {
        return reason;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}
