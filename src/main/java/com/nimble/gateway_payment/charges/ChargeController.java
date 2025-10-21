package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateOutputDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeUseCase chargeUseCase;

    public ChargeController(ChargeUseCase chargeUseCase) {
        this.chargeUseCase = chargeUseCase;
    }

    @PostMapping
    private ResponseEntity<ChargeCreateOutputDto> create(@RequestBody @Valid ChargeCreateInputDto body, @CookieValue(name = "userId") String userId) {
        this.chargeUseCase.create(body, UUID.fromString(userId));
        return ResponseEntity.status(200).body(new ChargeCreateOutputDto("Charge created with success."));
    }
}
