package com.example.demo;

import com.example.demo.model.UserTestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.launcher.TagFilter.includeTags;

public class SomeApplication {

    private final static Logger logger = LoggerFactory.getLogger(SomeApplication.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0){
            throw  new RuntimeException("data is error");
        }
        String basePath = args[0];
        ClassLoaderHandler classLoaderHandler = new ClassLoaderHandler(basePath);
        ClassLoader classLoader = classLoaderHandler.getClassLoader();
        UserTestResult userTestResult = classLoaderHandler.invoke(classLoader, (classRootPaths) -> {
            TestRunner testRunner = new TestRunner();
            return testRunner.runTests(classRootPaths, "1234");
        });
        String data = new ObjectMapper().writeValueAsString(userTestResult);
        saveToDatabase(data);
    }

    public static void saveToDatabase(String userTestResult){
        Document document = Document.parse(userTestResult);
        ConnectionString connectionString = new ConnectionString("mongodb+srv://demo:demodemo@apper.o6iaaqc.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("sample");
        MongoCollection<Document> testResult = database.getCollection("testResult");
        testResult.insertOne(document);
    }



}