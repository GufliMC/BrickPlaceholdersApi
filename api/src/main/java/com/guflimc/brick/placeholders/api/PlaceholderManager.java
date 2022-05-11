package com.guflimc.brick.placeholders.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PlaceholderManager<T> {

    /**
     * Replace placeholders in a component for the given entity.
     * @param entity for whom to replace placeholders
     * @param component to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull T entity, @NotNull Component component);

    /**
     * Replace placeholders in a component for the given player.
     * @param entity for whom to replace placeholders
     * @param text to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull T entity, @NotNull String text);

    /**
     * Get a list of registered placeholders.
     * @return a list of registered placeholders
     */
    Collection<String> placeholders();

    /**
     * Register a replacer function for a placeholder.
     *
     * The function is in the following format: (entity, placeholder) -> component.
     *
     * @param key the placeholder for the replacer function
     * @param replacer the replacer function
     */
    void registerReplacer(@NotNull String key, @NotNull BiFunction<T, String, Component> replacer);

    /**
     * Register a replacer function for a placeholder.
     *
     * The function is in the following format: (entity) -> component.
     *
     * @param key the placeholder for the replacer function
     * @param replacer the replacer function
     */
    void registerReplacer(@NotNull String key, @NotNull Function<T, Component> replacer);

    /**
     * Unregister a placeholder.
     * @param key the placeholder to unregister
     */
    void unregisterReplacer(@NotNull String key);

}