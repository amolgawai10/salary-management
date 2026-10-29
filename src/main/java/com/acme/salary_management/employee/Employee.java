package com.acme.salary_management.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false, length = 50, updatable = false)
    private String employeeCode;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(name = "work_country", nullable = false, length = 2)
    private String workCountry;

    @Column(nullable = false)
    private boolean active;

    @Version
    @Column(nullable = false)
    private long version;

    protected Employee() {
    }

    public Employee(
            String employeeCode,
            String fullName,
            String department,
            String workCountry,
            boolean active
    ) {
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.department = department;
        this.workCountry = workCountry;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public String getWorkCountry() {
        return workCountry;
    }

    public boolean isActive() {
        return active;
    }

    public long getVersion() {
        return version;
    }
}
