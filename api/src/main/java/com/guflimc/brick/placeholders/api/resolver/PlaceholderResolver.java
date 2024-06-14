package com.guflimc.brick.placeholders.api.resolver;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderResolver<E> {

    @Nullable Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context);

    //

    static <E, R> PlaceholderResolver<E> requireEntity(@NotNull PlaceholderResolver<E> resolver) {
        return (placeholder, context) -> {
            if (context.entity() == null) {
                return null;
            }
            return resolver.resolve(placeholder, context);
        };
    }

    static <E, R> PlaceholderResolver<E> requireViewer(@NotNull PlaceholderResolver<E> resolver) {
        return (placeholder, context) -> {
            if (context.viewer() == null) {
                return null;
            }
            return resolver.resolve(placeholder, context);
        };
    }

}