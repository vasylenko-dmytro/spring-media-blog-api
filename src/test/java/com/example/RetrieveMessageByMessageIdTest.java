package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.example.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RetrieveMessageByMessageIdTest {
    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;

    /**
     * Before every test, reset the database, restart the Javalin app, and create a new webClient
     * and ObjectMapper for interacting locally on the web.
     */
    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        String[] args = new String[]{};
        app = SpringApplication.run(SocialMediaApp.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }

    /**
     * Sending an http request to GET localhost:8080/messages/1
     * <p>
     * Expected Response:
     * Status Code: 200
     * Response Body: JSON representation of a message object
     */
    @Test
    public void getMessageGivenMessageIdMessageFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/9999"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        Message expectedResult = new Message(9999, 9999, "test message 1", 1669947792L);
        Message actualResult = objectMapper.readValue(response.body(), Message.class);
        assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }


    /**
     * Sending an http request to GET localhost:8080/messages/100 (message id 100 does not exist)
     * <p>
     * Expected Response:
     * Status Code: 200
     * Response Body:
     */
    @Test
    public void getMessageGivenMessageIdMessageNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/100"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        assertTrue(response.body().isEmpty(), "Expected Empty Result, but Result was not Empty");
    }
}
