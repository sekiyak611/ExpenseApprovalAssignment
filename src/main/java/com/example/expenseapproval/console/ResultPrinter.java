package com.example.expenseapproval.console;

import java.io.PrintStream;
import java.util.Objects;

import com.example.expenseapproval.approval.ApprovalResult;
import com.example.expenseapproval.common.InvalidInputException;
import com.example.expenseapproval.submission.SubmissionResult;

/**
 * 申請受付結果・承認結果をコンソールへ出力する。出力形式のみを担い、結果の判定は行わない。
 */
public final class ResultPrinter {

    private final PrintStream out;

    public ResultPrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out);
    }

    public void printSubmissionResult(SubmissionResult result) {
        switch (result) {
            case SubmissionResult.Accepted accepted -> out.printf("[申請受付] %s：受付成功（%s）%n",
                    accepted.applicationId(), accepted.status());
            case SubmissionResult.Rejected rejected -> out.printf("[申請受付] %s：受付不可 %s%n",
                    rejected.applicationId(), rejected.errors());
        }
    }

    public void printApprovalResult(ApprovalResult result) {
        switch (result) {
            case ApprovalResult.Approved approved -> out.printf("[承認] %s：承認成功（%s、承認者：%s）%n",
                    approved.applicationId(), approved.status(), approved.approverId());
            case ApprovalResult.Failed failed -> out.printf("[承認] %s：承認不可 %s%n",
                    failed.applicationId(), failed.error());
        }
    }

    public void printInputError(InvalidInputException exception) {
        out.printf("[入力不備] %s%n", exception.getMessage());
    }
}
