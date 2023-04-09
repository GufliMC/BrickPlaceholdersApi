package com.guflimc.brick.placeholders.api.module;

import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderModule<E> extends PlaceholderResolver<E, Object> {

    @NotNull String name();

}
