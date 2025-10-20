package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
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
public class AuthLoginControllerTest {

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
    }

    private void createUser(RegisterInputDto dto) {
        var passwordHash = this.passwordEncoder.encode(dto.getPassword());
        dto.setPassword(passwordHash);
        var user = new UserEntity(dto);
        this.userRepository.saveAndFlush(user);
    }

    @Test
    public void shouldReturn400IfIdentifierNotInformed() throws Exception {
        LoginInputDto dto = LoginInputDto.builder().password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The identifier field requires email or CPF."));
    }

    @Test
    public void shouldReturn400IfPasswordNotInformed() throws Exception {
        LoginInputDto dto = LoginInputDto.builder().identifier("josiemerson@email.com").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The password field require."));
    }

    @Test
    public void shouldReturn400IfIdentifierIsEmpty() throws Exception {
        LoginInputDto dto = LoginInputDto.builder().identifier("").password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The identifier field requires email or CPF."));
    }

    @Test
    public void shouldReturn400IfPasswordIsEmpty() throws Exception {
        LoginInputDto dto = LoginInputDto.builder()
                .identifier("josiemerson@email.com")
                .password("").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The password field require."));
    }

    @Test
    public void shouldReturn400IfIdentifierNotIsEmailOrCpf() throws Exception {
        LoginInputDto dto = LoginInputDto.builder()
                .identifier("identifierInvalid")
                .password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Identifier invalid."));
    }

    @Test
    public void shouldReturn400IfIdentifierByEmailNotExists() throws Exception {
        LoginInputDto dto = LoginInputDto.builder()
                .identifier("email@email.com")
                .password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Identifier or password incorrect."));
    }

    @Test
    public void shouldReturn400IfIdentifierByCpfNotExists() throws Exception {
        LoginInputDto dto = LoginInputDto.builder()
                .identifier("12233344540")
                .password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Identifier or password incorrect."));
    }

    @Test
    public void shouldReturn400IfPasswordIncorrect() throws Exception {
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("38485789300")
                .password("passwordIncorrect")
                .build();
        this.createUser(registerDto);

        LoginInputDto dto = LoginInputDto.builder()
                .identifier("josi1@email.com")
                .password("123456").build();
        this.mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Identifier or password incorrect."));
    }
}
