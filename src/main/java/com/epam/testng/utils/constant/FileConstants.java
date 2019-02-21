package com.epam.testng.utils.constant;

public enum FileConstants {
    FILE_EXTENSION_SEPARATOR("."),
    JIRA_KEY_PREFIX("_");

    private final String value;

    FileConstants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
