package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthRegisterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        this.userRepository.deleteAll();
    }

    private void createUser(RegisterInputDto dto) {
        var passwordHash = this.passwordEncoder.encode(dto.getPassword());
        dto.setPassword(passwordHash);
        var user = new UserEntity(dto);
        this.userRepository.saveAndFlush(user);
    }

    @Test
    public void shouldReturn400IfNameNotInformed() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .password("password1234")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The name field cannot be empty"));
    }

    @Test
    public void shouldReturn400IfEmailNotInformed() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .cpf("64717564294")
                .password("password1234")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The email field cannot be empty"));

    }

    @Test
    public void shouldReturn400IfEmailInvalid() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@.com")
                .cpf("64717564294")
                .password("password1234")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The email field cannot be invalid"));

    }

    @Test
    public void shouldReturn400IfCpfNotInformed() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .password("password1234")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The CPF field cannot be empty"));

    }

    @Test
    public void shouldReturn400IfCpfInvalid() throws Exception {
        String cpfWith12Digits = "123456789012";
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf(cpfWith12Digits)
                .password("password1234")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The CPF field cannot be invalid"));

    }

    @Test
    public void shouldReturn400IfPasswordNotInformed() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("64717564294")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The password field cannot be empty"));
    }

    @Test
    public void shouldReturn400IfPasswordHasLessThan6Digits() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("64717564294")
                .password("12345")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The Password field must have at least 6 digits"));
    }

    @Test
    public void shouldReturn400IfCpfCharacterInvalid() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("647.175.642-94")
                .password("123456")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The CPF must contain only numbers."));

    }

    @Test
    public void shouldReturn400IfExistsUserWithCpf() throws Exception {
        RegisterInputDto registerInputDto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerso@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        this.createUser(registerInputDto);
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerson@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User already exists"));
    }

    @Test
    public void shouldReturn400IfExistsUserWithEmail() throws Exception {
        RegisterInputDto registerInputDto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerson@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        this.createUser(registerInputDto);
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josiemerson@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User already exists"));

    }

    @Test
    public void shouldReturn200IfUserRegisterSuccess() throws Exception {
        RegisterInputDto dto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created with success."));
    }
}
