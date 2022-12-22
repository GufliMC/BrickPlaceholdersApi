package com.guflimc.brick.placeholders.api.extension;

import org.jetbrains.annotations.NotNull;

public interface PlaceholderExtension<T> extends PlaceholderReplacer<T> {

    @NotNull
    String id();

}
