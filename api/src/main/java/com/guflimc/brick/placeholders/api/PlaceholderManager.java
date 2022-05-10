package org.minestombrick.placeholders.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PlaceholderManager<T> {

    Component replace(@NotNull T T, @NotNull Component component);

    Component replace(@NotNull T T, @NotNull String text);

    Collection<String> placeholders();

    // function of format (T, key, string)
    void registerReplacer(@NotNull String key, @NotNull BiFunction<T, String, Component> replacer);

    // function of format (T, string)
    void registerReplacer(@NotNull String key, @NotNull Function<T, Component> replacer);

    void unregisterReplacer(@NotNull String key);

}