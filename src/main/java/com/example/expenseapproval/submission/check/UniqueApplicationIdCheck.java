package com.example.expenseapproval.submission.check;

import java.time.LocalDate;
import java.util.Objects;

import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 申請IDが登録済み申請と重複しないか。
 */
public final class UniqueApplicationIdCheck implements SubmissionCheck {

    private final ApplicationRepository applicationRepository;

    public UniqueApplicationIdCheck(ApplicationRepository applicationRepository) {
        this.applicationRepository = Objects.requireNonNull(applicationRepository);
    }

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.DUPLICATE_ID;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        return applicationRepository.findById(application.applicationId()).isEmpty();
    }
}
