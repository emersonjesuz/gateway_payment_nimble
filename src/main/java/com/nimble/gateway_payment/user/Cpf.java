package com.nimble.gateway_payment.user;

public class Cpf {
    private final String value;

    public Cpf(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The CPF cannot be null.");
        }
        String cleaned = value.trim();
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("The CPF must contain only numbers.");
        }
        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("The CPF must contain exactly 11 numeric digits.");
        }
        if (!isValidCpf(cleaned)) {
            throw new IllegalArgumentException("CPF inválid.");
        }
        this.value = cleaned;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return false;
        if (cpf.chars().distinct().count() == 1) return false;

        try {
            int[] digits = cpf.chars().map(c -> c - '0').toArray();
            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += digits[i] * (10 - i);
            }
            int check1 = 11 - (sum1 % 11);
            if (check1 > 9) check1 = 0;
            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += digits[i] * (11 - i);
            }
            int check2 = 11 - (sum2 % 11);
            if (check2 > 9) check2 = 0;
            return check1 == digits[9] && check2 == digits[10];
        } catch (Exception e) {
            return false;
        }
    }
}