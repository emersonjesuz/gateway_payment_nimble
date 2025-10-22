package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateOutputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeOutputDto;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/created")
    private ResponseEntity<List<ChargeOutputDto>> findAllByOriginator(@Param("status") Status status, @CookieValue(name = "userId") String userId) {
        var result = this.chargeUseCase.findAllCreateCharge(status, UUID.fromString(userId));
        return ResponseEntity.status(200).body(result);
    }
}
