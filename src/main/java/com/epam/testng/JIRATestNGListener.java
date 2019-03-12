package com.epam.testng;

import com.epam.testng.utils.resultconstructor.TestResultConstructor;
import com.epam.testng.utils.resultconstructor.TestResultConstructorManager;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.List;

public class JIRATestNGListener extends TestListenerAdapter implements IReporter{

    private TestResultConstructor testResultConstructor = TestResultConstructorManager.getInstance();

    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish(iTestContext);
        super.getSkippedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getPassedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getFailedTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        super.getFailedButWithinSuccessPercentageTests().forEach(iTestResult -> testResultConstructor.putTestResultData(iTestResult, iTestResult.getStatus()));
        testResultConstructor.proceedExcludedTestMethod(iTestContext);
    }

    @Override
    public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
        testResultConstructor.saveResultData();
    }
}