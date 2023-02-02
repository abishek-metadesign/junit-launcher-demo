package com.example.demo;

import com.example.demo.model.UserTestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SomeApplication {

    private final static Logger logger = LoggerFactory.getLogger(SomeApplication.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 2){
            throw  new RuntimeException("data is error");
        }
        String basePath = args[0];
        String url = args[1];
        ClassLoaderHandler classLoaderHandler = new ClassLoaderHandler(basePath);
        ClassLoader classLoader = classLoaderHandler.getClassLoader();
        UserTestResult userTestResult = classLoaderHandler.invoke(classLoader, (classRootPaths) -> {
            TestRunner testRunner = new TestRunner();
            return testRunner.runTests(classRootPaths, "1234");
        });
        String data = new ObjectMapper().writeValueAsString(userTestResult);
        System.out.println(data);
        saveToDataBase(data,url);
    }


    public static void saveToDataBase(String data,String url){
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest httprequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(url + "/testResult"))
                .header("content-type","application/json")
                .build();

        try {
            httpClient.send(httprequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }



}