package com.guflimc.brick.placeholders.common.modules;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.TypeConversionException;
import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.parser.PlaceholderParser;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class OperatorMapPlaceholderModule<E> extends OperatorPlaceholderModule<E> {

    public OperatorMapPlaceholderModule(PlaceholderManager<E> manager) {
        super(manager, "map");
    }

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
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

        try {
            Component resolved = manager.resolve(inner, context, Component.class);
            if (mappings.containsKey(resolved)) {
                return replace(mappings.get(resolved), resolved);
            }
            if (fallback != null) {
                return replace(fallback, resolved);
            }
            return resolved;
        } catch (TypeConversionException e) {
            throw new RuntimeException(e);
        }
    }

}
