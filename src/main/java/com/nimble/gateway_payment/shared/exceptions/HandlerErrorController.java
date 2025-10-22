package com.nimble.gateway_payment.shared.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandlerErrorController {
    private final MessageSource messageSource;

    public HandlerErrorController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidException(MethodArgumentNotValidException e, WebRequest request) {
        List<ErrorResponse> dto = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    e.getStatusCode().value(),
                    message,
                    request.getDescription(false));
            dto.add(error);
        });
        return new ResponseEntity<>(dto.get(0), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                e.getStatusCode(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<ErrorResponse> handlePaymentRequiredException(PaymentRequiredException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                e.getStatusCode(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                e.getStatusCode(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage() != null ? e.getMessage() : "Unexpected error occurred",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        String message = e.getMessage();
        if (e.getName().equals("status") && e.getRequiredType() != null && e.getRequiredType().isEnum()) {
            message = String.format(
                    "The value '%s' is not a valid status. Accepted values: %s",
                    e.getValue(),
                    String.join(", ", getEnumValues(e.getRequiredType()))
            );

        }
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                400,
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String[] getEnumValues(Class<?> enumType) {
        return (String[]) java.util.Arrays.stream(enumType.getEnumConstants())
                .map(Object::toString)
                .toArray(String[]::new);
    }
}
