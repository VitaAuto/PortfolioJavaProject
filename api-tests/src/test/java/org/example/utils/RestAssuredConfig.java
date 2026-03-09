package org.example.utils;

import io.restassured.RestAssured;

public class RestAssuredConfig {
    public static final String BASE_URI = "http://localhost";
    public static final int PORT = 8082;

    public static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = PORT;
    }
}