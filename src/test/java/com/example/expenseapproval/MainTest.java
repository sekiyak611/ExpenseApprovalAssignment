package com.example.expenseapproval;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void 確認ケースを実行し結果をコンソールへ出力する() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        assertEquals(List.of(
                "[申請受付] A001：受付成功（PENDING）",
                "[承認] A001：承認成功（APPROVED、承認者：U002）",
                "[申請受付] A002：受付不可 [INVALID_DATE, AMOUNT_LIMIT, RECEIPT_REQUIRED]",
                "[承認] A001：承認不可 INVALID_STATUS",
                "[申請受付] A003：受付成功（PENDING）",
                "[承認] A003：承認不可 NOT_AUTHORIZED",
                "[承認] A003：承認不可 SELF_APPROVAL",
                "[入力不備] 利用目的が入力されていません"),
                output.toString(StandardCharsets.UTF_8).lines().toList());
    }
}
