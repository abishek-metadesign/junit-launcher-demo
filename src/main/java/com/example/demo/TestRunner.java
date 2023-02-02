package com.example.demo;

import com.example.demo.model.UserTestResult;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.nio.file.Path;
import java.util.Set;

public class TestRunner {

    public UserTestResult runTests(Set<Path> paths, String userId) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(DiscoverySelectors.selectClasspathRoots(paths))
                .filters(ClassNameFilter.includeClassNamePatterns(".*"))
                .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                .configurationParameter("junit.jupiter.extensions.autodetection.enabled", "true")
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.discover(request);
        CustomTestListener customTestListener = new CustomTestListener(userId);
        launcher.registerTestExecutionListeners(customTestListener);
        launcher.execute(request);
        return customTestListener.getResult();
    }

}
