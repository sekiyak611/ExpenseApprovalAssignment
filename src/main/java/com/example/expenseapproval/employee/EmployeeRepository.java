package com.example.expenseapproval.employee;

import java.util.Optional;

/**
 * 社員一覧の保持・検索。
 */
public interface EmployeeRepository {

    Optional<Employee> findById(String employeeId);

    /**
     * 社員を登録する。同じ社員IDの社員が登録済みの場合は置き換える。
     */
    void save(Employee employee);
}
