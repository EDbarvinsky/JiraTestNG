package com.epam.testng.utils.resultconstructor;

import com.epam.jira.core.TestResultProcessor;
import com.epam.testng.JIRATestKey;
import com.epam.testng.utils.constant.JIRATestResult;
import com.epam.testng.utils.resultconstructor.mapper.CollectionMapper;
import com.epam.testng.utils.constant.AttachmentConstants;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epam.testng.utils.constant.AttachmentConstants.*;
import static java.util.concurrent.TimeUnit.*;

public class TestResultConstructor {
    private Map<String, Map<String, String>> results = new ConcurrentHashMap<>();
    private List<File> attachmentsList = new CopyOnWriteArrayList<>();
    private Map<String, Throwable> throwableMap = new ConcurrentHashMap<>();
    private CollectionMapper collectionMapper = new CollectionMapper();

    TestResultConstructor() {
    }

    public void saveResultData() {
        results = collectionMapper.map(results, attachmentsList);
        results = collectionMapper.map(results, throwableMap);
        writeResultData();
        TestResultProcessor.saveResults();
    }

    public void collectAttachment(File file) {
        attachmentsList.add(file);
    }

    public void proceedExcludedTestMethod(ITestContext iTestContext) {
        for (ITestNGMethod testNGMethod : iTestContext.getExcludedMethods()) {
            if (hasEnabledJIRATestKeyAnnotation(testNGMethod)) {
                results.put(getJIRATestKey(testNGMethod), new HashMap<>());
                results.get(getJIRATestKey(testNGMethod)).put(STATUS.toString(),getJIRATestStatus(5));
                results.get(getJIRATestKey(testNGMethod)).put(SUMMARY.toString(), "Disabled or ignored test method");
            }
        }
    }

    public void putTestResultData(ITestResult testResult, int statusId) {
        if (hasEnabledJIRATestKeyAnnotation(testResult.getMethod())) {
            results.put(getJIRATestKey(testResult.getMethod()), new HashMap<>());
            results.get(getJIRATestKey(testResult.getMethod())).put(TIME.toString(), getTestDuration(testResult));
            results.get(getJIRATestKey(testResult.getMethod())).put(STATUS.toString(), getJIRATestStatus(statusId));
            if (testResult.getThrowable() != null) {
                throwableMap.put(getJIRATestKey(testResult.getMethod()), testResult.getThrowable());
            }
        }
    }

    private void writeResultData() {
        for (String resultsKey : results.keySet()) {
            TestResultProcessor.startJiraAnnotatedTest(resultsKey);
            for (String entryKey : results.get(resultsKey).keySet()) {
                setData(resultsKey, entryKey);
            }
        }
    }

    private void setData(String key, String entryKey) {
        String attachmentKey = "";
        if (entryKey.contains(ATTACHMENT.toString())) {
            attachmentKey = entryKey;
            entryKey = entryKey.substring(0, ATTACHMENT.toString().length());
        }
        switch (AttachmentConstants.valueOf(entryKey.toUpperCase())) {
            case TIME:
                TestResultProcessor.setTime(results.get(key).get(entryKey));
                break;
            case STATUS:
                TestResultProcessor.setStatus(results.get(key).get(entryKey));
                break;
            case EXCEPTION:
                TestResultProcessor.addException(new Throwable(results.get(key).get(entryKey)));
                break;
            case SUMMARY:
                TestResultProcessor.addToSummary(results.get(key).get(entryKey));
                break;
            case ATTACHMENT:
                TestResultProcessor.addAttachment(new File(results.get(key).get(attachmentKey)));
                break;
            default:
                throw new IllegalArgumentException("No case for '" + entryKey + "' in switch-case statement");
        }
    }

    private boolean hasEnabledJIRATestKeyAnnotation(ITestNGMethod testNGMethod) {
        return testNGMethod.getConstructorOrMethod().getMethod().isAnnotationPresent(JIRATestKey.class) &&
                !testNGMethod.getConstructorOrMethod().getMethod().getAnnotation(JIRATestKey.class).disabled();
    }

    private String getJIRATestKey(ITestNGMethod testNGMethod) {
        if (hasEnabledJIRATestKeyAnnotation(testNGMethod)) {
            return testNGMethod.getConstructorOrMethod().getMethod().getAnnotation(JIRATestKey.class).key();
        } else
            throw new IllegalArgumentException("JIRATestKey annotation is disabled on test method " + testNGMethod.getConstructorOrMethod().getName());
    }

    private String getTestDuration(ITestResult iTestResult) {
        long duration = iTestResult.getEndMillis() - iTestResult.getStartMillis();
        return MINUTES.convert(duration, MICROSECONDS) +
                "m " +
                SECONDS.convert(duration, MICROSECONDS) +
                "." +
                MILLISECONDS.convert(duration, MICROSECONDS) +
                "s ";
    }

    private String getJIRATestStatus(int statusId) {
        switch (statusId) {
            case 1:
                return JIRATestResult.PASSED.toString();
            case 2:
                return JIRATestResult.FAILED.toString();
            case 3:
                return JIRATestResult.UNTESTED.toString();
            case 4:
                return JIRATestResult.FAILED.toString();
            case 5:
                return JIRATestResult.OUT_OF_SCOPE.toString();
            default:
                throw new IllegalArgumentException("Illegal test method status id number. No mapping for " + statusId + " value");
        }
    }
}