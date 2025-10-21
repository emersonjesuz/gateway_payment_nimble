package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ChargeCreateControllerTest {

    private UserEntity userMock;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ChargeRepository chargeRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private void createUser(RegisterInputDto dto) {
        userMock = new UserEntity(dto);
        this.userRepository.saveAndFlush(userMock);
    }

    @Test
    public void shouldReturn400IfRecipientCpfNotInformed() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().amount(BigDecimal.valueOf(200)).build();
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.createUser(registerDto);
        this.mvc.perform(post("/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .cookie(new Cookie("userId", this.userMock.getId().toString()))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The recipientCpf field cannot be empty"));

    }
}
