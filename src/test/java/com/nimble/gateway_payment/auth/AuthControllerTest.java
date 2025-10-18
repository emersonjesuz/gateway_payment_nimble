package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterOutputDto;
import com.nimble.gateway_payment.shared.exceptions.ErrorResponse;
import com.nimble.gateway_payment.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    private void createUserInDatabase(String name, String email, String cpf, String password) {
        RegisterInputDto userRegister = RegisterInputDto.builder()
                .name(name)
                .email(email)
                .cpf(cpf)
                .password(password)
                .build();
        this.restTemplate.postForEntity("/auth/register", userRegister, ErrorResponse.class);
    }

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

    @Test
    public void shouldReturn400IfCpfInvalid() {
        String cpfWith12Digits = "123456789012";
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf(cpfWith12Digits)
                .password("password1234")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The CPF field cannot be invalid", response.message());
    }

    @Test
    public void shouldReturn400IfPasswordNotInformed() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("64717564294")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The password field cannot be empty", response.message());
    }

    @Test
    public void shouldReturn400IfPasswordHasLessThan6Digits() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("64717564294")
                .password("12345")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The Password field must have at least 6 digits", response.message());
    }

    @Test
    public void shouldReturn400IfCpfCharacterInvalid() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("647.175.642-94")
                .password("123456")
                .build();
        ErrorResponse response = this.restTemplate.postForObject("/auth/register", dto, ErrorResponse.class);
        assertEquals(400, response.status());
        assertEquals("The CPF must contain only numbers.", response.message());
    }

    @Test
    public void shouldReturn400IfExistsUserWithCpf() {
        this.createUserInDatabase("josi", "jos@email.com", "11501002902", "123456");
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerson@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        ResponseEntity<ErrorResponse> response = this.restTemplate.postForEntity("/auth/register", dto, ErrorResponse.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody().message());
    }

    @Test
    public void shouldReturn400IfExistsUserWithEmail() {
        this.createUserInDatabase("josi", "josiemerson@email.com", "11501002902", "123456");
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerson@email.com")
                .cpf("64717564294")
                .password("123456")
                .build();
        ResponseEntity<ErrorResponse> response = this.restTemplate.postForEntity("/auth/register", dto, ErrorResponse.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody().message());
    }

    @Test
    public void shouldReturn200IfUserRegisterSuccess() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        ResponseEntity<RegisterOutputDto> response = this.restTemplate.postForEntity("/auth/register", dto, RegisterOutputDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created with success.", response.getBody().message());
    }
}
