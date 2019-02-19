import com.epam.jira.core.TestResultProcessor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.*;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

public class JIRATestNGListener extends TestListenerAdapter {

    @Override
    public void onStart(ITestContext iTestContext) {
        super.onStart(iTestContext);
        List<ITestNGMethod> testMethodsList = iTestContext.getExcludedMethods().stream()
                .filter(this::hasJIRATestKeyAnnotation).collect(Collectors.toList());
        for (ITestNGMethod testNGMethod : testMethodsList) {
                TestResultProcessor.startJiraAnnotatedTest(getJIRATestKey(testNGMethod));
                TestResultProcessor.setStatus(JIRATestResult.UNTESTED.toString());
                TestResultProcessor.addToSummary("Disabled or ignored method");
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        super.onTestStart(iTestResult);
        if (hasJIRATestKeyAnnotation(iTestResult.getMethod())) {
            TestResultProcessor.startJiraAnnotatedTest(getJIRATestKey(iTestResult.getMethod()));
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        super.onTestSuccess(iTestResult);
        if (hasJIRATestKeyAnnotation(iTestResult.getMethod())) {
            TestResultProcessor.setTime(getTestDuration(iTestResult));
            TestResultProcessor.setStatus(JIRATestResult.PASSED.toString());
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        super.onTestFailure(iTestResult);
        if (hasJIRATestKeyAnnotation(iTestResult.getMethod())) {
            TestResultProcessor.setTime(getTestDuration(iTestResult));
            TestResultProcessor.setStatus(JIRATestResult.FAILED.toString());
            TestResultProcessor.addException(iTestResult.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        super.onTestSkipped(iTestResult);
        if (hasJIRATestKeyAnnotation(iTestResult.getMethod())) {
            TestResultProcessor.setTime(getTestDuration(iTestResult));
            TestResultProcessor.setStatus(JIRATestResult.UNTESTED.toString());
            TestResultProcessor.addException(iTestResult.getThrowable());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        super.onTestFailedButWithinSuccessPercentage(iTestResult);
        if (hasJIRATestKeyAnnotation(iTestResult.getMethod())) {
            TestResultProcessor.setTime(getTestDuration(iTestResult));
            TestResultProcessor.setStatus(JIRATestResult.FAILED.toString());
            TestResultProcessor.addException(iTestResult.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish(iTestContext);
        TestResultProcessor.saveResults();
    }

    private boolean hasJIRATestKeyAnnotation(ITestNGMethod testNGMethod) {
        return testNGMethod.getConstructorOrMethod().getMethod().isAnnotationPresent(JIRATestKey.class);
    }

    private String getJIRATestKey(ITestNGMethod testNGMethod){
        return testNGMethod.getConstructorOrMethod().getMethod().getAnnotation(JIRATestKey.class).key();
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
}
