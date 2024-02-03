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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteMessageByMessageIdTest {
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
     * Sending an http request to DELETE localhost:8080/messages/1 (message exists)
     * <p>
     * Expected Response:
     * Status Code: 200
     * Response Body: count of rows modified (should only modify a single row)
     */
    @Test
    public void deleteMessageGivenMessageIdMessageFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/9999"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        Integer actualResult = objectMapper.readValue(response.body(), Integer.class);
        assertEquals(1, (int) actualResult, "Expected to modify 1 row, but actually modified " + actualResult + " rows.");
    }

    /**
     * Sending an http request to DELETE localhost:8080/messages/100 (message does NOT exists)
     * <p>
     * Expected Response:
     * Status Code: 200
     * Response Body:
     */
    @Test
    public void deleteMessageGivenMessageIdMessageNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/100"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        String actualResult = response.body();
        assertEquals("", actualResult, "Expected empty response body, but actually " + actualResult + ".");
    }
}
