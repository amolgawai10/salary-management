package com.acme.salary_management.seed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SplittableRandom;

public final class EmployeeSeedGenerator {

    public static final int DEFAULT_EMPLOYEE_COUNT = 10_000;
    public static final long DEFAULT_RANDOM_SEED = 20_251_029L;

    private static final LocalDate LATEST_INITIAL_SALARY_DATE = LocalDate.of(2025, 1, 1);
    private static final int INITIAL_SALARY_DATE_RANGE_DAYS = 5 * 365;

    private static final String[] FIRST_NAMES = {
            "Aarav", "Aisha", "Amelia", "Arjun", "Asha", "Ava", "Benjamin", "Charlotte",
            "Daniel", "Elena", "Ethan", "Fatima", "Grace", "Hannah", "Haruto", "Isabella",
            "Jack", "James", "Jia", "Liam", "Lucas", "Maya", "Mia", "Muhammad", "Noah",
            "Oliver", "Olivia", "Priya", "Ravi", "Sakura", "Samuel", "Sofia", "Sophia",
            "Theo", "William", "Yuki", "Zara"
    };

    private static final String[] LAST_NAMES = {
            "Anderson", "Brown", "Chen", "Davis", "Garcia", "Gupta", "Harris", "Ito",
            "Johnson", "Jones", "Khan", "Kim", "Kumar", "Lee", "Martin", "Miller", "Moore",
            "Murphy", "Nguyen", "Patel", "Robinson", "Rodriguez", "Sato", "Singh", "Smith",
            "Suzuki", "Taylor", "Thomas", "Thompson", "Walker", "Wang", "Williams", "Wilson"
    };

    private static final String[] DEPARTMENTS = {
            "Engineering", "Finance", "Human Resources", "Legal", "Marketing", "Operations",
            "Product", "Sales", "Security", "Support"
    };

    private static final CountryPayProfile[] COUNTRY_PAY_PROFILES = {
            new CountryPayProfile("AU", "AUD", 65_000, 240_000),
            new CountryPayProfile("CA", "CAD", 60_000, 220_000),
            new CountryPayProfile("DE", "EUR", 50_000, 200_000),
            new CountryPayProfile("FR", "EUR", 45_000, 190_000),
            new CountryPayProfile("GB", "GBP", 40_000, 180_000),
            new CountryPayProfile("IN", "INR", 600_000, 4_500_000),
            new CountryPayProfile("SG", "SGD", 55_000, 230_000),
            new CountryPayProfile("US", "USD", 55_000, 250_000)
    };

    private final long randomSeed;

    public EmployeeSeedGenerator() {
        this(DEFAULT_RANDOM_SEED);
    }

    public EmployeeSeedGenerator(long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public List<EmployeeSeedRecord> generate() {
        return generate(DEFAULT_EMPLOYEE_COUNT);
    }

    public List<EmployeeSeedRecord> generate(int employeeCount) {
        if (employeeCount < 0) {
            throw new IllegalArgumentException("employeeCount must not be negative");
        }

        SplittableRandom random = new SplittableRandom(randomSeed);
        List<EmployeeSeedRecord> employees = new ArrayList<>(employeeCount);

        for (int index = 1; index <= employeeCount; index++) {
            CountryPayProfile payProfile = randomElement(COUNTRY_PAY_PROFILES, random);
            employees.add(new EmployeeSeedRecord(
                    "ACME-%05d".formatted(index),
                    randomElement(FIRST_NAMES, random) + " " + randomElement(LAST_NAMES, random),
                    randomElement(DEPARTMENTS, random),
                    payProfile.countryCode(),
                    random.nextInt(100) < 95,
                    randomSalary(payProfile, random),
                    payProfile.currencyCode(),
                    LATEST_INITIAL_SALARY_DATE.minusDays(random.nextInt(INITIAL_SALARY_DATE_RANGE_DAYS)),
                    "Initial salary"
            ));
        }

        return List.copyOf(employees);
    }

    private static BigDecimal randomSalary(CountryPayProfile payProfile, SplittableRandom random) {
        long minimumHundreds = payProfile.minimumSalary() / 100;
        long maximumHundreds = payProfile.maximumSalary() / 100;
        long amount = random.nextLong(minimumHundreds, maximumHundreds + 1) * 100;
        return BigDecimal.valueOf(amount).setScale(4, RoundingMode.UNNECESSARY);
    }

    private static <T> T randomElement(T[] values, SplittableRandom random) {
        return values[random.nextInt(values.length)];
    }

    private record CountryPayProfile(
            String countryCode,
            String currencyCode,
            long minimumSalary,
            long maximumSalary
    ) {
        private CountryPayProfile {
            countryCode = countryCode.toUpperCase(Locale.ROOT);
            currencyCode = currencyCode.toUpperCase(Locale.ROOT);
        }
    }
}
