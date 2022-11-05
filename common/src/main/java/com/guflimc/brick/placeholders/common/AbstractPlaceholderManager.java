package com.guflimc.brick.placeholders.common;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public abstract class AbstractPlaceholderManager<T> implements PlaceholderManager<T> {

    private final static Pattern PATTERN = Pattern.compile("(\\{[^}]+})");
    private final Map<String, BiFunction<T, String, Component>> replacers = new HashMap<>();

    @Override
    public Component replace(@NotNull T T, @NotNull String text) {
        return replace(T, Component.text(text));
    }

    @Override
    public Component replace(@NotNull T T, @NotNull Component component) {
        return component
                .replaceText(builder -> builder.match(PATTERN)
                .replacement((matchResult, textbuilder) -> replace(T, matchResult, textbuilder)));
    }

    private ComponentLike replace(T T, MatchResult match, TextComponent.Builder builder) {
        String group = match.group().toLowerCase();
        String placeholder = group.substring(1, group.length()-1); // remove surrounding brackets

        if ( !replacers.containsKey(placeholder) ) {
            return builder;
        }

        Component replacement = replacers.get(placeholder).apply(T, placeholder);
        if ( replacement == null ) {
            replacement = Component.text("");
        }

        return replacement;
    }

    @Override
    public Collection<String> placeholders() {
        return Collections.unmodifiableSet(replacers.keySet());
    }

    @Override
    public void registerReplacer(@NotNull String key, @NotNull BiFunction<T, String, Component> replacer) {
        replacers.put(key.toLowerCase(), replacer);
    }

    @Override
    public void registerReplacer(@NotNull String key, @NotNull Function<T, Component> replacer) {
        replacers.put(key.toLowerCase(), (p, s) -> replacer.apply(p));
    }

    @Override
    public void unregisterReplacer(@NotNull String key) {
        replacers.remove(key.toLowerCase());
    }
}