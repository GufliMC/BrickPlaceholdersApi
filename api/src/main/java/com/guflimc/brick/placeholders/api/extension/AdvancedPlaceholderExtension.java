package com.guflimc.brick.placeholders.api.extension;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface AdvancedPlaceholderExtension<T> extends PlaceholderExtension<T> {

    <D> void addTypeParser(@NotNull Class<D> dataType, @NotNull Function<String, D> parser);

    //

    void removeReplacer(@NotNull String name);

    //

    void addReplacer(@NotNull String name, @NotNull PlaceholderReplacer<T> replacer);

    <D> void addReplacer(@NotNull String name, @NotNull Class<D> dataType, @NotNull AdvancedPlaceholderReplacer<T, D> replacer);
}