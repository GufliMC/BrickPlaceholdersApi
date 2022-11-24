package com.guflimc.brick.placeholders.common.handler;

import com.guflimc.brick.placeholders.api.PlaceholderReplacer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface PlaceholderHandler<T> extends PlaceholderReplacer<T> {

    @NotNull Pattern pattern();

}
