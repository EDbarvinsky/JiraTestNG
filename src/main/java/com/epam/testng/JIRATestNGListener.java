package com.epam.testng;

import com.epam.testng.utils.resultconstructor.TestResultConstructor;
import com.epam.testng.utils.resultconstructor.TestResultConstructorManager;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

public class JIRATestNGListener extends TestListenerAdapter {

    private TestResultConstructor testResultConstructor = TestResultConstructorManager.getInstance();

    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish(iTestContext);
        super.getSkippedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getPassedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getFailedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getFailedButWithinSuccessPercentageTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        testResultConstructor.proceedExcludedTestMethod(iTestContext);
        testResultConstructor.saveResultData();
    }
}