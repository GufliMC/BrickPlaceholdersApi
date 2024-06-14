package com.guflimc.brick.placeholders.api.module;

import org.jetbrains.annotations.NotNull;

public class PlaceholderTreeModule<E> extends PlaceholderModuleResolver<E> implements PlaceholderModule<E> {

    private final String name;

    public PlaceholderTreeModule(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String name() {
        return name;
    }


}
