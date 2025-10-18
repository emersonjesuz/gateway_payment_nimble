package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.shared.exceptions.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn400IfNameNotInformed() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .password("password1234")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The name field cannot be empty", response.message());
    }

    @Test
    public void shouldReturn400IfEmailNotInformed() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .cpf("64717564294")
                .password("password1234")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The email field cannot be empty", response.message());
    }

    @Test
    public void shouldReturn400IfEmailInvalid() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@.com")
                .cpf("64717564294")
                .password("password1234")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The email field cannot be invalid", response.message());
    }

    @Test
    public void shouldReturn400IfCpfNotInformed() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .password("password1234")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The CPF field cannot be empty", response.message());
    }
}
