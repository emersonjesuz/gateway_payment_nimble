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
import java.util.UUID;

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
        this.userRepository.deleteAll();
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

    @Test
    public void shouldReturn400IfRecipientCpfIsInvalid() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("invalid cpf").amount(BigDecimal.valueOf(200)).build();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The recipientCpf field cannot be invalid"));
    }

    @Test
    public void shouldReturn400IfAmountNotInformed() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("38485789300").build();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The amount field cannot be empty"));
    }

    @Test
    public void shouldReturn400IfAmountIsLessThanEqualZero() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("38485789300").amount(BigDecimal.valueOf(0)).build();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Amount must be greater than 0"));
    }

    @Test
    public void shouldReturn400IfAmountIsGreaterThan10Thousand() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("38485789300").amount(BigDecimal.valueOf(10001)).build();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Amount must not exceed R$ 10,000"));
    }

    @Test
    public void shouldReturn400IfRecipientCpfCharacterInvalid() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("384.857.893-00").amount(BigDecimal.valueOf(100)).build();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The CPF must contain only numbers."));
    }

    @Test
    public void shouldReturn404IfExistsRecipientUserWithCpf() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("38485789300").amount(BigDecimal.valueOf(100)).build();
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        this.createUser(registerDto);
        this.mvc.perform(post("/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .cookie(new Cookie("userId", this.userMock.getId().toString()))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found."));
    }

    @Test
    public void shouldReturn404IfExistsOriginatorUserWithCpf() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("11501002902").amount(BigDecimal.valueOf(100)).build();
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("11501002902")
                .password("123456")
                .build();
        this.createUser(registerDto);
        this.mvc.perform(post("/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(dto))
                        .cookie(new Cookie("userId", UUID.randomUUID().toString()))
                        .header("Authorization", TestUtils.generatedToken(this.userMock))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found."));
    }

    @Test
    public void shouldReturn400IfOriginatorEqualRecipient() throws Exception {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("11501002902").amount(BigDecimal.valueOf(100)).build();
        RegisterInputDto registerDto = RegisterInputDto.builder()
                .name("josi")
                .email("josi1@email.com")
                .cpf("11501002902")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The originator cannot be the same as the recipient."));
    }
}
