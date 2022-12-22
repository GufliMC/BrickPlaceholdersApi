package com.guflimc.brick.placeholders.api;

import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderExtension;
import com.guflimc.brick.placeholders.api.extension.PlaceholderExtension;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderManager<T> {

    /**
     * Replace placeholders in a component for the given entity.
     * @param entity for whom to replace placeholders
     * @param component to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull T entity, @NotNull Component component);

    /**
     * Replace placeholders in a string for the given player.
     * @param entity for whom to replace placeholders
     * @param text to replace placeholders in
     * @return the component with placeholders replaced
     */
    Component replace(@NotNull T entity, @NotNull String text);

    /**
     * Return the replaced component for the given placeholder.
     * @param entity for whom to replace placeholders.
     * @param placeholder the full name of the placeholder to replace.
     * @return the component that matches the given placeholder.
     */
    Component replacement(@NotNull T entity, @NotNull String placeholder);

    /**
     * Register an extension that provides placeholders.
     *
     * @param extension the extension to register
     */
    void registerExtension(@NotNull PlaceholderExtension<T> extension);

    /**
     * Register an extension that provides placeholders.
     * The extension will be of the {@link AdvancedPlaceholderExtension} type and will be created by this manager.
     *
     * @param id the id of the extension
     * @return the extension that was created
     */
    AdvancedPlaceholderExtension<T> registerExtension(@NotNull String id);

    /**
     * Unregister a placeholder extension
     * @param id the id of the extension to unregister.
     */
    void unregisterExtension(@NotNull String id);

}