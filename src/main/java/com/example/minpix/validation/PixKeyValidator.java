package com.example.minpix.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;
import java.util.regex.Pattern;

public class PixKeyValidator implements ConstraintValidator<ValidPixKey, PixKeyPayload> {

    private static final Pattern CNPJ_FORMAT = Pattern.compile("\\d{14}");
    private static final Pattern EMAIL_FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE_FORMAT = Pattern.compile("^\\+[1-9]\\d{7,14}$");
    private static final Pattern UUID_FORMAT =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");

    @Override
    public boolean isValid(PixKeyPayload payload, ConstraintValidatorContext context) {
        if (payload == null || payload.pixKeyType() == null || payload.pixKeyValue() == null) {
            return true;
        }

        String type = payload.pixKeyType().toUpperCase(Locale.ROOT);
        String value = payload.pixKeyValue().trim();

        boolean valid = switch (type) {
            case "CPF" -> CpfValidationSupport.isValid(value);
            case "CNPJ" -> isValidCnpj(value);
            case "EMAIL" -> value.length() <= 254 && EMAIL_FORMAT.matcher(value).matches();
            case "TELEFONE" -> PHONE_FORMAT.matcher(value).matches();
            case "ALEATORIA" -> isUuid(value);
            default -> false;
        };

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(payload.pixKeyValueField())
                    .addConstraintViolation();
        }
        return valid;
    }

    private static boolean isValidCnpj(String value) {
        String digits = value.replace(".", "").replace("/", "").replace("-", "");
        if (!CNPJ_FORMAT.matcher(digits).matches() || digits.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateCnpjDigit(digits, 12);
        int secondDigit = calculateCnpjDigit(digits, 13);
        return digits.charAt(12) - '0' == firstDigit
                && digits.charAt(13) - '0' == secondDigit;
    }

    private static int calculateCnpjDigit(String digits, int length) {
        int[] weights = length == 12
                ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int index = 0; index < length; index++) {
            sum += (digits.charAt(index) - '0') * weights[index];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private static boolean isUuid(String value) {
        return UUID_FORMAT.matcher(value).matches();
    }
}
