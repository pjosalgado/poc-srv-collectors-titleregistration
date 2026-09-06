package dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final List<ErrorDetail> errors;

    @Getter
    @AllArgsConstructor
    public static class ErrorDetail {
        private final String code;
        private final String title;
        private final String detail;
    }

    public static ErrorResponse of(String code, String title, String detail) {
        return new ErrorResponse(List.of(new ErrorDetail(code, title, detail)));
    }

    public static ErrorResponse of(List<ErrorDetail> errors) {
        return new ErrorResponse(errors);
    }
}
