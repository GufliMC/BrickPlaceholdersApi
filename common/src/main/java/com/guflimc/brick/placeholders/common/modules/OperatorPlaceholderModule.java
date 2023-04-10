package com.guflimc.brick.placeholders.common.modules;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.ReplacementConversionException;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.parser.PlaceholderParser;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class OperatorPlaceholderModule<E> implements PlaceholderModule<E> {

    private final PlaceholderManager<E> manager;

    public OperatorPlaceholderModule(PlaceholderManager<E> manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String name() {
        return "op";
    }

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        if (placeholder.startsWith("map_")) {
            return _map(placeholder.substring("map_".length()), context);
        }
        if (placeholder.startsWith("if_")) {
            return _if(placeholder.substring("if_".length()), context);
        }
        return null;
    }

    // map

    private Object _map(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        PlaceholderParser parser = PlaceholderParser.of(placeholder);

        String inner = parser.until('_', 1);

        Map<String, String> mappings = new HashMap<>();
        String fallback = null;

        while ( parser.hasNext() ) {
            // key
            String key = parser.until(':', 1);

            if ( !parser.hasNext() ) {
                fallback = key;
                break;
            }

            String value = parser.until('_', 1);
            mappings.put(key, value);
        }

        Object resolved = manager.resolve(inner, context);
        if ( resolved instanceof Component c ) {
            return _map_component(c, mappings, fallback);
        }
        return _map_string(resolved.toString(), mappings, fallback);
    }

    private String _map_string(String resolved, Map<String, String> mappings, String fallback) {
        if ( mappings.containsKey(resolved) ) {
            return replace(mappings.get(resolved), resolved);
        }
        if ( fallback != null ) {
            return replace(fallback, resolved);
        }
        return resolved;
    }

    private Component _map_component(Component resolved, Map<String, String> mappings, String fallback) {
        // TODO
        return null;
    }

    // if

    private Object _if(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        if ( placeholder.startsWith("present_") ) {
            return _if_present(placeholder.substring("present_".length()), context);
        }
        return null;
    }

    // if present

    private Object _if_present(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        PlaceholderParser parser = PlaceholderParser.of(placeholder);

        String inner = parser.until('_', 1);

        String _then = parser.until('_', 1);
        String _else = null;
        if ( parser.hasNext() ) {
            _else = parser.until('_', 1);
        }

        Object resolved = manager.resolve(inner, context);
        if ( resolved instanceof Component c ) {
            return _if_present_component(c, _then, _else);
        }
        return _if_present_string(resolved != null ? resolved.toString() : null, _then, _else);
    }

    private String _if_present_string(String resolved, String _then, String _else) {
        if ( resolved != null ) {
            return replace(_then, resolved);
        }
        return _else;
    }

    private Component _if_present_component(Component resolved, String _then, String _else) {
        // TODO
        return null;
    }

    // utils

    private Component replace(@NotNull Component replacement, @NotNull Component original) {
        return replacement.replaceText(builder -> builder.match(Pattern.quote("{}"))
                .replacement((mr, tb) -> original));
    }

    private String replace(@NotNull String replacement, @NotNull String original) {
        return replacement.replace("{}", original);
    }

}
