package com.guflimc.brick.placeholders.common;

import com.guflimc.brick.placeholders.api.PlaceholderReplacer;
import com.guflimc.brick.placeholders.common.handler.PlaceholderHandler;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class BrickPlaceholderHandler<T> implements PlaceholderHandler<T> {

    private final static Pattern PATTERN = Pattern.compile("(\\{[^}]+})");
    private final Map<String, PlaceholderReplacer<T>> replacers = new HashMap<>();

    @Override
    public @NotNull Pattern pattern() {
        return PATTERN;
    }

    @Override
    public Component replace(@NotNull T entity, @NotNull String key) {
        if (!replacers.containsKey(key)) {
            return null;
        }

        Component replacement = replacers.get(key).replace(entity, key);
        if (replacement == null) {
            replacement = Component.text("");
        }

        return replacement;
    }

    //

    public Collection<String> placeholders() {
        return Collections.unmodifiableSet(replacers.keySet());
    }

    public void registerReplacer(@NotNull String key, @NotNull PlaceholderReplacer<T> replacer) {
        replacers.put(key.toLowerCase(), replacer);
    }

    public void registerReplacer(@NotNull String key, @NotNull Function<T, Component> replacer) {
        replacers.put(key.toLowerCase(), (p, s) -> replacer.apply(p));
    }

    public void unregisterReplacer(@NotNull String key) {
        replacers.remove(key.toLowerCase());
    }

}
