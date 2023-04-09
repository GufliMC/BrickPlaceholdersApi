package com.guflimc.brick.placeholders.api;

import com.guflimc.brick.placeholders.api.exception.ReplacementConversionException;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderManager<E> {

    /**
     * Replace placeholders.
     * @param component to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull Component component, @NotNull PlaceholderResolveContext<E> context);

    /**
     * Replace placeholders with the given entity as context.
     * @param entity for whom to replace placeholders
     * @param component to replace placeholders in
     * @return the component with placeholders replaced
     */
    default Component replace(@NotNull Component component, @NotNull E entity) {
        return replace(component, PlaceholderResolveContext.of(entity));
    }

    /**
     * Replace placeholders.
     * @param text to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull String text, @NotNull PlaceholderResolveContext<E> context);

    /**
     * Replace placeholders with the given entity as context.
     * @param entity for whom to replace placeholders
     * @param text to replace placeholders in
     * @return the component with placeholders replaced
     */
    default Component replace(@NotNull String text, @NotNull E entity) {
        return replace(text, PlaceholderResolveContext.of(entity));
    }

    //

    /**
     * Resolve the placeholder replacement for the given context.
     * @return the replacement.
     */
    Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context);

    /**
     * Resolve the placeholder replacement for the given context. This will try to convert the replacement to the given type.
     * @return the replacement.
     */
    <R> R resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context, @NotNull Class<R> type)
            throws ReplacementConversionException;

    /**
     * Resolve the placeholder replacement for the given entity as context.
     * @param entity for whom to replace placeholders.
     * @param placeholder the placeholder to replace
     * @return the replacement.
     */
    default Object resolve(@NotNull String placeholder, @NotNull E entity) {
        return resolve(placeholder, PlaceholderResolveContext.of(entity));
    }

    /**
     * Resolve the placeholder replacement for the given entity as context. This will try to convert the replacement to the given type.
     * @param entity for whom to replace placeholders.
     * @param placeholder the placeholder to replace
     * @return the replacement.
     */
    default <R> R resolve(@NotNull String placeholder, @NotNull E entity, @NotNull Class<R> type)
            throws ReplacementConversionException {
        return resolve(placeholder, PlaceholderResolveContext.of(entity), type);
    }

    //

    /**
     * Register a module that provides placeholders.
     * @param module the module to register
     */
    void register(@NotNull PlaceholderModule<E> module);

    /**
     * Unregister a placeholder module
     * @param module the module to unregister.
     */
    void unregister(@NotNull PlaceholderModule<E> module);

}