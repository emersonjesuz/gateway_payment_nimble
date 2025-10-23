package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.accountBank.dtos.DepositInputDto;
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
public class AccountBankDepositControllerTest {

    private UserEntity userMock;

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
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.userRepository.deleteAll();
    }

    private void createUser(RegisterInputDto dto) {
        userMock = new UserEntity(dto);
        this.userRepository.saveAndFlush(userMock);
    }

    @Test
    public void shouldReturn400IfAmountNotInformed() throws Exception {
        DepositInputDto dto = new DepositInputDto(null);
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.createUser(registerDto);

        var a = this.mvc.perform(post("/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The amount field cannot be empty"));
    }

    @Test
    public void shouldReturn400IfAmountIsLessThanOrEqualToZero() throws Exception {
        DepositInputDto dto = new DepositInputDto(BigDecimal.valueOf(0));
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.createUser(registerDto);

        this.mvc.perform(post("/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Amount must be greater than 0"));
    }

    @Test
    public void shouldReturn400IfAmountIsGreaterThanTenThousand() throws Exception {
        DepositInputDto dto = new DepositInputDto(BigDecimal.valueOf(10001));
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.createUser(registerDto);

        this.mvc.perform(post("/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Amount must not exceed R$ 10,000"));
    }

    @Test
    public void shouldReturn201IfDepositSuccess() throws Exception {
        DepositInputDto dto = new DepositInputDto(BigDecimal.valueOf(500));
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi@email.com")
                .cpf("38485789300")
                .password("123456")
                .build();
        this.createUser(registerDto);

        this.mvc.perform(post("/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJSON(dto))
                .header("Authorization", TestUtils.generatedToken(this.userMock))
        ).andReturn();
    }
}
