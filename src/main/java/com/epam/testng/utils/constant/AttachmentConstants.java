package com.epam.testng.utils.constant;

public enum AttachmentConstants {
    ATTACHMENT("ATTACHMENT"),
    EXCEPTION("EXCEPTION"),
    TIME("TIME"),
    STATUS("STATUS"),
    SUMMARY("SUMMARY");

    private final String value;

    AttachmentConstants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
