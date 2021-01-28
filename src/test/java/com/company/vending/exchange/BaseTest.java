package com.company.vending.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class BaseTest {
    public static MockWebServer mockBackEnd;
    public ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

}
