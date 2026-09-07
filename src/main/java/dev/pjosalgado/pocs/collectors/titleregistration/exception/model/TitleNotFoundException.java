package dev.pjosalgado.pocs.collectors.titleregistration.exception.model;

public class TitleNotFoundException extends RuntimeException {

    public TitleNotFoundException(String titleId) {
        super("Title not found: " + titleId);
    }

}
