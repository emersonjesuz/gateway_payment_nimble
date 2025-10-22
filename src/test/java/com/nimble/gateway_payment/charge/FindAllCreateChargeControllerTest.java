package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.ChargeListToMock;
import com.nimble.gateway_payment.TestUtils;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FindAllCreateChargeControllerTest {

    private UserEntity user1;
    private UserEntity user2;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void beforeAll() {
        user1 = userRepository.saveAndFlush(UserEntity.builder()
                .cpf("11111111111")
                .name("João")
                .email("joao@email.com")
                .password("123456")
                .build());

        user2 = userRepository.saveAndFlush(UserEntity.builder()
                .cpf("22222222222")
                .name("Maria")
                .email("maria@email.com")
                .password("123456")
                .build());

        chargeRepository.saveAllAndFlush(ChargeListToMock.data(user1, user1, user2, user2));
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldReturn400IfStatusInvalid() throws Exception {
        this.mvc.perform(get("/charge/created?status=INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestUtils.generatedToken(user1))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("The value 'INVALID' is not a valid status. Accepted values: PENDING, PAID, CANCELED"));
    }
}
