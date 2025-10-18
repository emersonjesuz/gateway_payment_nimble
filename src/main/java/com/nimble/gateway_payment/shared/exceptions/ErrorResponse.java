package com.nimble.gateway_payment.shared.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String message, String path) {
}
