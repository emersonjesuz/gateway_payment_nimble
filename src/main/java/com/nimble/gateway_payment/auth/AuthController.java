package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.auth.dtos.LoginOutputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterOutputDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterOutputDto> register(@RequestBody @Valid RegisterInputDto body) {
        this.authUseCase.register(body);
        return ResponseEntity.status(200).body(new RegisterOutputDto("User created with success."));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOutputDto> login(@RequestBody @Valid LoginInputDto body) {
        var token = this.authUseCase.login(body);
        return ResponseEntity.status(200).body(new LoginOutputDto(token));
    }
}
