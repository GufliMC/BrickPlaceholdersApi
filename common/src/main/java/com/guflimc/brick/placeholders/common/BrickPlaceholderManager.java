package com.guflimc.brick.placeholders.common;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderExtension;
import com.guflimc.brick.placeholders.api.extension.PlaceholderExtension;
import com.guflimc.brick.placeholders.common.extension.BrickAdvancedPlaceholderExtension;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class BrickPlaceholderManager<T> implements PlaceholderManager<T> {

    private final static Pattern PATTERN = Pattern.compile("(\\{[^}]+})");

    private final Map<String, PlaceholderExtension<T>> extensions = new ConcurrentHashMap<>();

    @Override
    public Component replace(@NotNull T T, @NotNull String text) {
        return replace(T, Component.text(text));
    }

    @Override
    public Component replace(@NotNull T T, @NotNull Component component) {
        return component.replaceText(builder -> builder.match(PATTERN)
                .replacement((mr, tb) -> replace(T, mr)));
    }

    private ComponentLike replace(T entity, MatchResult match) {
        String group = match.group().toLowerCase();
        String placeholder = group.substring(1, group.length() - 1); // remove surrounding brackets
        return replacement(entity, placeholder);
    }

    //

    @Override
    public Component replacement(@NotNull T entity, @NotNull String placeholder) {
        for (String id : extensions.keySet().stream().sorted(Comparator.reverseOrder()).toList()) {
            if (!placeholder.startsWith(id)) {
                continue;
            }

            String data = placeholder.substring(id.length());
            if (data.startsWith("_")) {
                data = data.substring(1);
            }

            return extensions.get(id).replace(entity, data);
        }
        return null;
    }

    //

    @Override
    public void registerExtension(@NotNull PlaceholderExtension<T> extension) {
        if (!extension.id().matches("[a-z0-9]+")) {
            throw new IllegalArgumentException("Extension id may only contain lowercase letters and numbers.");
        }
        extensions.put(extension.id(), extension);
    }

    @Override
    public AdvancedPlaceholderExtension<T> registerExtension(@NotNull String id) {
        BrickAdvancedPlaceholderExtension<T> extension = new BrickAdvancedPlaceholderExtension<>(id);
        registerExtension(extension);
        return extension;
    }

    @Override
    public void unregisterExtension(@NotNull String id) {
        extensions.remove(id);
    }
}