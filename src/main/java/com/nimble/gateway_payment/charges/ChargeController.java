package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateOutputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeOutputDto;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.enums.TypeCharge;
import com.nimble.gateway_payment.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeUseCase chargeUseCase;

    public ChargeController(ChargeUseCase chargeUseCase) {
        this.chargeUseCase = chargeUseCase;
    }

    @PostMapping
    private ResponseEntity<ChargeCreateOutputDto> create(@RequestBody @Valid ChargeCreateInputDto body, @AuthenticationPrincipal UserEntity user) {
        this.chargeUseCase.create(body, user.getId());
        return ResponseEntity.status(200).body(new ChargeCreateOutputDto("Charge created with success."));
    }

    @GetMapping("/created")
    private ResponseEntity<List<ChargeOutputDto>> findAllCreateCharge(@Param("status") Status status, @AuthenticationPrincipal UserEntity user) {
        var result = this.chargeUseCase.findAllCharge(status, user.getId(), TypeCharge.ORIGINATOR);
        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/receive")
    private ResponseEntity<List<ChargeOutputDto>> findAllReceiveCharge(@Param("status") Status status, @AuthenticationPrincipal UserEntity user) {
        var result = this.chargeUseCase.findAllCharge(status, user.getId(), TypeCharge.RECIPIENT);
        return ResponseEntity.status(200).body(result);
    }
}
