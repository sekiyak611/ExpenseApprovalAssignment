package com.example.expenseapproval.employee;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 社員一覧をメモリ上で保持する {@link EmployeeRepository}。
 */
public final class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<String, Employee> employees = new LinkedHashMap<>();

    @Override
    public Optional<Employee> findById(String employeeId) {
        return Optional.ofNullable(employees.get(employeeId));
    }

    @Override
    public void save(Employee employee) {
        Objects.requireNonNull(employee);
        employees.put(employee.id(), employee);
    }
}
