package com.school.school_management_api.exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("La solicitud contiene datos inválidos o una regla de negocio no cumplida.");
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}