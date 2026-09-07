package dev.pjosalgado.pocs.collectors.titleregistration.exception.handler;

import dev.pjosalgado.pocs.collectors.openapi.model.ErrorDetail;
import dev.pjosalgado.pocs.collectors.openapi.model.ErrorResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TitleExceptionHandler {

    @ExceptionHandler(TitleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(TitleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .errors(List.of(ErrorDetail.builder()
                                .code("NOT_FOUND")
                                .title("Title not found")
                                .detail(ex.getMessage())
                                .build()))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErrorDetail.builder()
                        .code("VALIDATION_ERROR")
                        .title("Invalid field: " + fieldError.getField())
                        .detail(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().errors(errors).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errors(List.of(ErrorDetail.builder()
                                .code("BAD_REQUEST")
                                .title("Invalid request")
                                .detail(ex.getMessage())
                                .build()))
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .errors(List.of(ErrorDetail.builder()
                                .code("INTERNAL_ERROR")
                                .title("Unexpected error")
                                .detail(ex.getMessage())
                                .build()))
                        .build());
    }
}
