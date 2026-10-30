package com.example.minpix.validation;

final class CpfValidationSupport {

    private CpfValidationSupport() {
    }

    static boolean isValid(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        String digits = cpf.replace(".", "").replace("-", "");
        if (!digits.matches("\\d{11}") || digits.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(digits, 9);
        int secondDigit = calculateDigit(digits, 10);
        return digits.charAt(9) - '0' == firstDigit
                && digits.charAt(10) - '0' == secondDigit;
    }

    private static int calculateDigit(String digits, int length) {
        int sum = 0;
        for (int index = 0; index < length; index++) {
            sum += (digits.charAt(index) - '0') * (length + 1 - index);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
