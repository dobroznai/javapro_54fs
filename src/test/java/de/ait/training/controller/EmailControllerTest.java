package de.ait.training.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

//Запускает приложение целиком на случайном порту
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Использует тестовый профиль и базу H2
@ActiveProfiles("test")
public class EmailControllerTest {
    //Упрощённый HTTP-клиент, встроенный в Spring Boot
    @Autowired
    private TestRestTemplate restTemplate;
    //Подставляет фактический порт, на котором работает встроенный сервер
    @LocalServerPort
    private int port;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ------------------- POST /api/email/service -------------------

    @Test
    // Добавляет читаемое описание теста в отчётах
    @DisplayName("POST /api/email/service - valid email, status OK")
    void testShouldSendEmailSuccessfully() {
        // given
        String emailTo = "test@example.com";
        String requestUrl = url("/api/email/service?emailTo=" + emailTo);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, null, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Email sent to " + emailTo);
    }

    @Test
    @DisplayName("POST /api/email/service - invalid email, status BAD_REQUEST")
    void testShouldReturn400WhenEmailIsInvalid() {
        // given
        String invalidEmail = "invalid-email";
        String requestUrl = url("/api/email/service?emailTo=" + invalidEmail);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, null, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}