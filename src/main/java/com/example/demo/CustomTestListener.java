package com.example.demo;

import com.example.demo.model.Position;
import com.example.demo.model.TestResult;
import com.example.demo.model.TestStatus;
import com.example.demo.model.UserTestResult;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestTag;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CustomTestListener implements TestExecutionListener {

    private final List<TestResult> testResults;
    private UserTestResult userTestResult;

    public CustomTestListener(String userId) {
        userTestResult = new UserTestResult(userId);
        testResults = userTestResult.getTestResults();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        TestExecutionResult.Status status = testExecutionResult.getStatus();
        TestResult testResult = new TestResult();
        switch (status) {
            case SUCCESSFUL -> {
                testResult.setTestStatus(TestStatus.PASSED);
                extractTag(testIdentifier, testResult);
            }
            case FAILED -> {
                testResult.setTestStatus(TestStatus.FAILED);
                Optional<Throwable> throwable = testExecutionResult.getThrowable();
                throwable.ifPresent(value -> testResult.setErrorMessage(value.getMessage()));
                extractTag(testIdentifier, testResult);
            }
            case ABORTED -> {

            }
        }
        Set<TestTag> tags = testIdentifier.getTags();
        if (tags.size() != 0) {
            testResults.add(testResult);
        }
    }

    private static void extractTag(TestIdentifier testIdentifier, TestResult testResult) {
        Set<TestTag> tags = testIdentifier.getTags();
        for (TestTag tag : tags) {
            String name = tag.getName();
            String[] split = name.split("=");
            if (split.length == 2) {
                String key = split[0];
                String value = split[1];
                switch (key.toLowerCase()) {
                    case "position":
                        testResult.setPosition(Position.SENIOR);
                        break;
                    case "points":
                        testResult.setPoints(Integer.valueOf(value));
                        break;
                    case "name":
                        testResult.setName(value);
                        break;
                }
            }

        }
    }


    public UserTestResult getResult() {
        return userTestResult;
    }


}
