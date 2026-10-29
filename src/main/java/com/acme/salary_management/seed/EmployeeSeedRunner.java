package com.acme.salary_management.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class EmployeeSeedRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSeedRunner.class);

    private final EmployeeSeedService seedService;

    public EmployeeSeedRunner(EmployeeSeedService seedService) {
        this.seedService = seedService;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        EmployeeSeedService.SeedResult result = seedService.seed();
        LOGGER.info(
                "Employee seed completed: generated={}, employees={} -> {}, salary revisions={} -> {}",
                result.generatedEmployees(),
                result.employeesBefore(),
                result.employeesAfter(),
                result.salariesBefore(),
                result.salariesAfter()
        );
    }
}
