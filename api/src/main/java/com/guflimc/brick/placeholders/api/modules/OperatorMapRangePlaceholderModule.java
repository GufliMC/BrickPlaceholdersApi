package com.guflimc.brick.placeholders.api.modules;

import com.guflimc.brick.placeholders.api.manager.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.TypeConversionException;
import com.guflimc.brick.placeholders.api.parser.PlaceholderParser;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class OperatorMapRangePlaceholderModule<E> extends OperatorPlaceholderModule<E> {

    public OperatorMapRangePlaceholderModule(PlaceholderManager<E> manager) {
        super(manager, "map_range");
    }


    private record Range(double min, double max) {}

    @Override
    public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        PlaceholderParser parser = PlaceholderParser.of(placeholder);

        String inner = parser.until('_', 1);

        Map<Range, Component> mappings = new TreeMap<>(Comparator.comparingDouble(Range::min).reversed());
        Component fallback = null;

        while (parser.hasNext()) {
            // key
            String key = parser.until(':', 1);

            if (!parser.hasNext()) {
                fallback = parse(key);
                break;
            }

            try {
                String[] parts = key.split(Pattern.quote("-"));
                Range range = range(parts);

                Component value = parse(parser.until('_', 1));
                mappings.put(range, value);
            } catch (NumberFormatException ignore) {
            }
        }

        Component resolved = manager.resolve(inner, context);
        try {
            double value = parse(resolved);
            for (Range range : mappings.keySet()) {
                if (range.min <= value && value <= range.max) {
                    return replace(mappings.get(range), resolved);
                }
            }
        } catch (Exception ignored) {}

        if (fallback != null) {
            return replace(fallback, resolved);
        }
        return resolved;
    }

    @NotNull
    private static Range range(String[] parts) {
        double min, max;
        if ( parts[0].startsWith("<") ) {
            max = Double.parseDouble(parts[0].substring(1));
            min = Double.NEGATIVE_INFINITY;
        } else if ( parts[0].startsWith(">") ) {
            min = Double.parseDouble(parts[0].substring(1));
            max = Double.POSITIVE_INFINITY;
        } else {
            min = max = Double.parseDouble(parts[0]);
            if (parts.length > 1) {
                max = Double.parseDouble(parts[1]);
            }
        }

        return new Range(min, max);
    }

    private double parse(Component component) {
        try {
            if (component instanceof TextComponent tc) {
                return Double.parseDouble(tc.content());
            }
        } catch (Exception ignored) {}

        throw new RuntimeException("Could not convert component to number");
    }

}
