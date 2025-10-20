package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void shouldReturn400IfIdentifierNotInformed() throws Exception {
        LoginInputDto dto = LoginInputDto.builder().password("123456").build();
        this.mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJSON(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturn400IfPasswordNotInformed() throws Exception {
        LoginInputDto dto = LoginInputDto.builder().identifier("josiemerson@email.com").build();
        this.mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJSON(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
