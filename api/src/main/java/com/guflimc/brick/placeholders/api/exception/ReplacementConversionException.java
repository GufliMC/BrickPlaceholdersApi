package com.guflimc.brick.placeholders.api.exception;

import org.jetbrains.annotations.NotNull;

public class ReplacementConversionException extends Exception {

    private final String placeholder;
    private final Object replacement;
    private final Class<?> type;

    public ReplacementConversionException(@NotNull String placeholder, @NotNull Object replacement, @NotNull Class<?> type) {
        super(String.format("The replacement of placeholder '%s' with type %s could not be converted to type %s",
                placeholder, replacement.getClass().getSimpleName(), type.getSimpleName()));
        this.placeholder = placeholder;
        this.replacement = replacement;
        this.type = type;
    }

    public String placeholder() {
        return placeholder;
    }

    public Object replacement() {
        return replacement;
    }

    public Class<?> type() {
        return type;
    }

}
