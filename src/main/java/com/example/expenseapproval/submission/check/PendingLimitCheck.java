package com.example.expenseapproval.submission.check;

import java.time.LocalDate;
import java.util.Objects;

import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.RegisteredApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 申請者の承認待ち申請が上限件数に達していないか（追加仕様）。
 */
public final class PendingLimitCheck implements SubmissionCheck {

    private final ApplicationRepository applicationRepository;

    /** 申請者1人あたりの承認待ち申請の上限件数。登録済みの承認待ち申請がこの件数以上あれば受け付けない */
    private final int pendingLimit;

    public PendingLimitCheck(ApplicationRepository applicationRepository, int pendingLimit) {
        if (pendingLimit <= 0) {
            throw new IllegalArgumentException("上限件数は1以上を指定してください：" + pendingLimit);
        }
        this.applicationRepository = Objects.requireNonNull(applicationRepository);
        this.pendingLimit = pendingLimit;
    }

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.PENDING_LIMIT;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        long pendingCount = applicationRepository.findByApplicantId(application.applicantId()).stream()
                .filter(RegisteredApplication::isPending)
                .count();
        return pendingCount < pendingLimit;
    }
}
