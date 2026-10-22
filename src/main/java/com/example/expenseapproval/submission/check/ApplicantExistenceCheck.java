package com.example.expenseapproval.submission.check;

import java.time.LocalDate;
import java.util.Objects;

import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 申請者IDが社員一覧に存在するか。
 */
public final class ApplicantExistenceCheck implements SubmissionCheck {

    private final EmployeeRepository employeeRepository;

    public ApplicantExistenceCheck(EmployeeRepository employeeRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
    }

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.APPLICANT_NOT_FOUND;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        return employeeRepository.findById(application.applicantId()).isPresent();
    }
}
