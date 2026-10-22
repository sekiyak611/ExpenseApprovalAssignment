package com.example.expenseapproval.console;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.expenseapproval.approval.ApprovalErrorCode;
import com.example.expenseapproval.approval.ApprovalResult;
import com.example.expenseapproval.common.InvalidInputException;
import com.example.expenseapproval.expense.ApplicationStatus;
import com.example.expenseapproval.submission.SubmissionErrorCode;
import com.example.expenseapproval.submission.SubmissionResult;

class ResultPrinterTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    private final ResultPrinter printer = new ResultPrinter(new PrintStream(output, true, StandardCharsets.UTF_8));

    @Test
    void 受付成功は申請IDと状態を出力する() {
        printer.printSubmissionResult(new SubmissionResult.Accepted("A001", ApplicationStatus.PENDING));

        assertEquals(List.of("[申請受付] A001：受付成功（PENDING）"), outputLines());
    }

    @Test
    void 受付不可は申請IDとエラーコードの一覧を出力する() {
        printer.printSubmissionResult(new SubmissionResult.Rejected("A002",
                List.of(SubmissionErrorCode.INVALID_DATE, SubmissionErrorCode.AMOUNT_LIMIT)));

        assertEquals(List.of("[申請受付] A002：受付不可 [INVALID_DATE, AMOUNT_LIMIT]"), outputLines());
    }

    @Test
    void 承認成功は申請IDと状態と承認者を出力する() {
        printer.printApprovalResult(new ApprovalResult.Approved("A001", ApplicationStatus.APPROVED, "U002"));

        assertEquals(List.of("[承認] A001：承認成功（APPROVED、承認者：U002）"), outputLines());
    }

    @Test
    void 承認不可は申請IDとエラーコードを出力する() {
        printer.printApprovalResult(new ApprovalResult.Failed("A001", ApprovalErrorCode.INVALID_STATUS));

        assertEquals(List.of("[承認] A001：承認不可 INVALID_STATUS"), outputLines());
    }

    @Test
    void 入力不備はメッセージを出力する() {
        printer.printInputError(new InvalidInputException("利用目的が入力されていません"));

        assertEquals(List.of("[入力不備] 利用目的が入力されていません"), outputLines());
    }

    private List<String> outputLines() {
        return output.toString(StandardCharsets.UTF_8).lines().toList();
    }
}
