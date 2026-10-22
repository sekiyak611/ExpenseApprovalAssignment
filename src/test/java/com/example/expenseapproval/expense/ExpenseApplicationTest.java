package com.example.expenseapproval.expense;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.expenseapproval.common.InvalidInputException;

class ExpenseApplicationTest {

    @Test
    void 文字列項目は前後の空白を取り除いて保持する() {
        ExpenseApplication application = anApplication()
                .applicationId(" A001 ")
                .applicantId("　U001　")
                .purpose(" 客先訪問 ")
                .build();

        assertEquals("A001", application.applicationId());
        assertEquals("U001", application.applicantId());
        assertEquals("客先訪問", application.purpose());
    }

    @Test
    void 金額は1円から申請できる() {
        assertEquals(1, anApplication().amount(1).build().amount());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("入力不備のある申請")
    void 入力不備がある場合は例外で拒否する(Executable creation) {
        assertThrows(InvalidInputException.class, creation);
    }

    static Stream<Named<Executable>> 入力不備のある申請() {
        return Stream.of(
                invalid("申請IDがnull", () -> anApplication().applicationId(null).build()),
                invalid("申請IDが空文字", () -> anApplication().applicationId("").build()),
                invalid("申請IDが空白のみ", () -> anApplication().applicationId("   ").build()),
                invalid("申請者IDがnull", () -> anApplication().applicantId(null).build()),
                invalid("申請者IDが空白のみ", () -> anApplication().applicantId("　").build()),
                invalid("利用日がnull", () -> anApplication().usageDate(null).build()),
                invalid("経費区分がnull", () -> anApplication().category(null).build()),
                invalid("金額が0円", () -> anApplication().amount(0).build()),
                invalid("金額がマイナス", () -> anApplication().amount(-1).build()),
                invalid("利用目的がnull", () -> anApplication().purpose(null).build()),
                invalid("利用目的が空白のみ", () -> anApplication().purpose("  ").build()));
    }

    private static Named<Executable> invalid(String description, Executable creation) {
        return Named.of(description, creation);
    }
}
