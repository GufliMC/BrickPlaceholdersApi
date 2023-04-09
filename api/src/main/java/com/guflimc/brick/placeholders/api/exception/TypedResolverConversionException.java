package com.guflimc.brick.placeholders.api.exception;

import org.jetbrains.annotations.NotNull;

public class TypedResolverConversionException extends RuntimeException {

    private final String placeholder;
    private final Class<?> type;

    public TypedResolverConversionException(@NotNull String placeholder, @NotNull Class<?> type) {
        super(String.format("Failed to convert placeholder '%s' to type %s for a typed resolver.",
                placeholder, type.getSimpleName()));
        this.placeholder = placeholder;
        this.type = type;
    }

    public @NotNull String data() {
        return placeholder;
    }

    public @NotNull Class<?> type() {
        return type;
    }

}
