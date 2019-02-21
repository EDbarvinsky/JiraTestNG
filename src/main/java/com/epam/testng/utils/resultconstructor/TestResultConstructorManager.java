package com.epam.testng.utils.resultconstructor;

public class TestResultConstructorManager {
    private static volatile TestResultConstructor instance;

    private TestResultConstructorManager() {
    }

    public static synchronized TestResultConstructor getInstance() {
        if (instance == null) {
            instance = new TestResultConstructor();
        }
        return instance;
    }
}
