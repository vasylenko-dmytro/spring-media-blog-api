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

public class CreateMessageTest {
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
     * Sending an http request to POST localhost:8080/messages with valid message credentials
     * <p>
     * Expected Response:
     * Status Code: 200
     * Response Body: JSON representation of message object
     */
    @Test
    public void createMessageSuccessful() throws IOException, InterruptedException {
        String json = "{\"posted_by\":9999,\"message_text\": \"hello message\",\"time_posted_epoch\": 1669947792}";
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        ObjectMapper om = new ObjectMapper();
        Message expectedResult = new Message(1, 9999, "hello message", 1669947792L);
        Message actualResult = om.readValue(response.body(), Message.class);
        assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }

    /**
     * Sending an http request to POST localhost:8080/messages with empty message
     * <p>
     * Expected Response:
     * Status Code: 400
     */
    @Test
    public void createMessageMessageTextBlank() throws IOException, InterruptedException {
        String json = "{\"posted_by\":9999,\"message_text\": \"\",\"time_posted_epoch\": 1669947792}";
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }


    /**
     * Sending an http request to POST localhost:8080/messages with message length greater than 254
     * <p>
     * Expected Response:
     * Status Code: 400
     * Response Body:
     */
    @Test
    public void createMessageMessageGreaterThan255() throws IOException, InterruptedException {
        String json = "{\"posted_by\":9999,"
                + "\"message_text\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\","
                + "\"time_posted_epoch\": 1669947792}";
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }

    /**
     * Sending an http request to POST localhost:8080/messages with a user id that doesnt exist in db
     * <p>
     * Expected Response:
     * Status Code: 400
     */
    @Test
    public void createMessageUserNotInDb() throws IOException, InterruptedException {
        String json = "{\"posted_by\":5050,\"message_text\": \"hello message\",\"time_posted_epoch\": 1669947792}";
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }
}
