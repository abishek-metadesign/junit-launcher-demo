package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class UserTestResult {

    private String userId;
    private List<TestResult> testResults;

    public UserTestResult(String userId) {
        this.userId = userId;
        this.testResults= new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }
}
