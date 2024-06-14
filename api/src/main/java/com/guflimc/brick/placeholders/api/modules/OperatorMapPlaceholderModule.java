package com.guflimc.brick.placeholders.api.modules;

import com.guflimc.brick.placeholders.api.manager.PlaceholderManager;
import com.guflimc.brick.placeholders.api.parser.PlaceholderParser;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OperatorMapPlaceholderModule<E> extends OperatorPlaceholderModule<E> {

    public OperatorMapPlaceholderModule(PlaceholderManager<E> manager) {
        super(manager, "map");
    }

    @Override
    public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        PlaceholderParser parser = PlaceholderParser.of(placeholder);

        String inner = parser.until('_', 1);

        Map<Component, Component> mappings = new HashMap<>();
        Component fallback = null;

        while ( parser.hasNext() ) {
            // key
            Component key = parse(parser.until(':', 1));

            if ( !parser.hasNext() ) {
                fallback = key;
                break;
            }

            Component value = parse(parser.until('_', 1));
            mappings.put(key, value);
        }

        Component resolved = manager.resolve(inner, context);
        if (mappings.containsKey(resolved)) {
            return replace(mappings.get(resolved), resolved);
        }
        if (fallback != null) {
            return replace(fallback, resolved);
        }
        return resolved;
    }

}
