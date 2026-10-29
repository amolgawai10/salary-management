package com.acme.salary_management.salary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRevisionRepository extends JpaRepository<SalaryRevision, Long> {

    List<SalaryRevision> findByEmployeeIdOrderByEffectiveDateDesc(Long employeeId);

    Optional<SalaryRevision> findFirstByEmployeeIdOrderByEffectiveDateDesc(Long employeeId);
}
