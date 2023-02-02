package com.example.demo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassLoaderHandler {

    private String basePath;

    public ClassLoaderHandler(String basePath) {
        this.basePath = basePath;
    }

    public ClassLoader getClassLoader(){
        Path projectClasses = Paths.get(basePath,"classes");
        ClassLoader classLoader = ReflectionUtils.createCustomClassLoader(Collections.singleton(projectClasses))
                .orElseThrow(() -> new RuntimeException("Cannot load Classes"));
        Path testClasses = Paths.get(basePath,"test-classes");
        return ReflectionUtils.createCustomClassLoader(Collections.singleton(testClasses),classLoader )
                .orElseThrow(()->new RuntimeException("Cannot load tests"));
    }


    public <T> T  invoke(ClassLoader classLoader, Function<Set<Path>,T> excute) throws Exception {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        Set<Path> allClasspathRootDirectories = getAllClasspathRootDirectories();
        T t = excute.apply(allClasspathRootDirectories);
        Thread.currentThread().setContextClassLoader(currentClassLoader);
        return t;
    }
    private  Set<Path> getAllClasspathRootDirectories() {
        String fullClassPath = System.getProperty("java.class.path");
        Set<Path> classPathRoots = Arrays.stream(fullClassPath.split(File.pathSeparator))
                .map(Paths::get)
                .filter(Files::isDirectory)
                .collect(Collectors.toSet());
        classPathRoots.add(Paths.get(basePath,"classes"));
        classPathRoots.add(Paths.get(basePath,"test-classes"));
        return classPathRoots;
    }


}
