package com.guflimc.brick.placeholders.api.extension;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface AdvancedPlaceholderReplacer<T, D> {
    @Nullable
    Component replace(@NotNull T entity, @Nullable D data);
}