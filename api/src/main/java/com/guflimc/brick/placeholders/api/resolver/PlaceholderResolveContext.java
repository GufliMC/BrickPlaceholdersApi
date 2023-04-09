package com.guflimc.brick.placeholders.api.resolver;

import org.jetbrains.annotations.Nullable;

public class PlaceholderResolveContext<E> {

    private final E entity;
    private final E viewer;

    private PlaceholderResolveContext(E entity, E viewer) {
        this.entity = entity;
        this.viewer = viewer;
    }

    public E entity() {
        return entity;
    }

    public E viewer() {
        return viewer;
    }

    //

    public static <E> PlaceholderResolveContext<E> of(@Nullable E entity, @Nullable E viewer) {
        return new PlaceholderResolveContext<>(entity, viewer);
    }

    public static <E> PlaceholderResolveContext<E> of(@Nullable E entity) {
        return new PlaceholderResolveContext<>(entity, null);
    }

    public static <E> PlaceholderResolveContext<E> of() {
        return new PlaceholderResolveContext<>(null, null);
    }

}
