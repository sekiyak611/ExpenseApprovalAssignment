package com.example.expenseapproval.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class InputValidatorTest {

    @Test
    void 文字列は前後の空白を取り除いて返す() {
        assertEquals("A001", InputValidator.requireText(" A001\t", "申請ID"));
    }

    @Test
    void 全角スペースも前後の空白として取り除く() {
        assertEquals("A001", InputValidator.requireText("　A001　", "申請ID"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "　", " \t " })
    void 文字列がnull_空文字_空白のみの場合は入力不備(String value) {
        InvalidInputException e = assertThrows(InvalidInputException.class,
                () -> InputValidator.requireText(value, "申請ID"));
        assertEquals("申請IDが入力されていません", e.getMessage());
    }

    @Test
    void 必須項目がnullの場合は入力不備() {
        InvalidInputException e = assertThrows(InvalidInputException.class,
                () -> InputValidator.requireNonNull(null, "利用日"));
        assertEquals("利用日が入力されていません", e.getMessage());
    }

    @Test
    void 必須項目がnullでなければそのまま返す() {
        LocalDate date = LocalDate.of(2026, 10, 5);
        assertSame(date, InputValidator.requireNonNull(date, "利用日"));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void 数値が0以下の場合は入力不備(int value) {
        assertThrows(InvalidInputException.class, () -> InputValidator.requirePositive(value, "金額"));
    }

    @Test
    void 数値が1以上ならそのまま返す() {
        assertEquals(1, InputValidator.requirePositive(1, "金額"));
    }
}
