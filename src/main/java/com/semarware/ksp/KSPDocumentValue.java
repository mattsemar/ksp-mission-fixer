package com.semarware.ksp;

public class KSPDocumentValue {
    private final String key;
    private final String value;

    public KSPDocumentValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\": \"%s\"", key, value);
    }
}
