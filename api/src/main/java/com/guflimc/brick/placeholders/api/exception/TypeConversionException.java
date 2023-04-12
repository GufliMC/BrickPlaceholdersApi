package com.guflimc.brick.placeholders.api.exception;

import org.jetbrains.annotations.NotNull;

public class TypeConversionException extends Exception {

    public TypeConversionException(@NotNull String message) {
        super(message);
    }

    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
