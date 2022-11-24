package com.guflimc.brick.placeholders.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderReplacer<T> {

    @Nullable Component replace(@NotNull T entity, @NotNull String placeholder);

}
