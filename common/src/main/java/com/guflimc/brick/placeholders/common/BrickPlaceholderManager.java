package com.guflimc.brick.placeholders.common;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.PlaceholderReplacer;
import com.guflimc.brick.placeholders.common.handler.PlaceholderHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public abstract class BrickPlaceholderManager<T> implements PlaceholderManager<T> {

    private final static Pattern PATTERN = Pattern.compile("(\\{[^}]+})");

    private final BrickPlaceholderHandler<T> defaultHandler = new BrickPlaceholderHandler<>();
    private final Set<PlaceholderHandler<T>> handlers = new HashSet<>();

    protected BrickPlaceholderManager() {
        registerHandler(defaultHandler);
    }

    @Override
    public Component replace(@NotNull T T, @NotNull String text) {
        return replace(T, Component.text(text));
    }

    @Override
    public Component replace(@NotNull T T, @NotNull Component component) {
        for (PlaceholderHandler<T> handler : handlers) {
            component = component.replaceText(builder -> builder.match(handler.pattern())
                    .replacement((matchResult, textbuilder) -> replace(T, handler, matchResult, textbuilder)));
        }

//        return component.replaceText(builder -> builder.match(PATTERN)
//                .replacement((matchResult, textbuilder) -> replace(T, defaultHandler, matchResult, textbuilder)));

        return component;
    }

    private ComponentLike replace(T entity, PlaceholderHandler<T> handler, MatchResult match, TextComponent.Builder builder) {
        String group = match.group().toLowerCase();
        String placeholder = group.substring(1, group.length() - 1); // remove surrounding brackets

//        if (replacement == null) {
//            return builder;
//        }
        return handler.replace(entity, placeholder);
    }

    //

    @Override
    public Component replacement(@NotNull T entity, @NotNull String key) {
        return defaultHandler.replace(entity, key);
    }

    @Override
    public Collection<String> placeholders() {
        return defaultHandler.placeholders();
    }

    @Override
    public void registerReplacer(@NotNull String key, @NotNull PlaceholderReplacer<T> replacer) {
        defaultHandler.registerReplacer(key, replacer);
    }

    @Override
    public void registerReplacer(@NotNull String key, @NotNull Function<T, Component> replacer) {
        defaultHandler.registerReplacer(key, replacer);
    }

    @Override
    public void unregisterReplacer(@NotNull String key) {
        defaultHandler.unregisterReplacer(key);
    }

    //

    public void registerHandler(@NotNull PlaceholderHandler<T> handler) {
        handlers.add(handler);
    }

    public void unregisterHandler(@NotNull PlaceholderHandler<T> handler) {
        handlers.remove(handler);
    }

}