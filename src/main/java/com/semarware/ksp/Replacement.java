package com.semarware.ksp;

public class Replacement {
    private final String invalidValue;
    private final String correctValue;

    public Replacement(String invalidValue, String correctValue) {
        this.invalidValue = invalidValue;
        this.correctValue = correctValue;
    }

    @Override
    public String toString() {
        return "Replacement{" +
                "invalidValue='" + invalidValue + '\'' +
                ", correctValue='" + correctValue + '\'' +
                '}';
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public String getCorrectValue() {
        return correctValue;
    }
}
