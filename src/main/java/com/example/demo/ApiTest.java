package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiTest {

    private final static Logger logger = LoggerFactory.getLogger(ApiTest.class);

    public void makeGetCall() {
        logger.info("Hitting Get Request !!");
        Assertions.assertEquals(1,1);
    }
}
