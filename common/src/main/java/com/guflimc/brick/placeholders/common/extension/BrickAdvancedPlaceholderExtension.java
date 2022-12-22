package com.guflimc.brick.placeholders.common.extension;

import com.guflimc.brick.i18n.api.time.DurationParser;
import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderExtension;
import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderReplacer;
import com.guflimc.brick.placeholders.api.extension.PlaceholderReplacer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BrickAdvancedPlaceholderExtension<T> implements AdvancedPlaceholderExtension<T> {

    private final static Map<Class<?>, Function<String, ?>> STATIC_PARSERS = new HashMap<>();

    static {
        STATIC_PARSERS.put(String.class, Function.identity());
        STATIC_PARSERS.put(Integer.class, Integer::parseInt);
        STATIC_PARSERS.put(Long.class, Long::parseLong);
        STATIC_PARSERS.put(Double.class, Double::parseDouble);
        STATIC_PARSERS.put(Float.class, Float::parseFloat);
        STATIC_PARSERS.put(Component.class, Component::text);
        STATIC_PARSERS.put(DecimalFormat.class, DecimalFormat::new);
        STATIC_PARSERS.put(DateTimeFormatter.class, DateTimeFormatter::ofPattern);
        STATIC_PARSERS.put(Boolean.class, s -> {
            if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y")) {
                return true;
            }
            if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n")) {
                return false;
            }
            return Boolean.parseBoolean(s);
        });
    }

    private static <D> Function<String, D> wrap(Function<String, D> function) {
        return s -> {
            try {
                return function.apply(s);
            } catch (Exception e) {
                return null;
            }
        };
    }

    //

    private final Map<String, PlaceholderReplacer<T>> replacers = new HashMap<>();
    private final Map<Class<?>, Function<String, ?>> parsers = new HashMap<>();

    private final String id;

    public BrickAdvancedPlaceholderExtension(String id) {
        this.id = id;
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public @Nullable Component replace(@NotNull T entity, @NotNull String data) {
        for (String name : replacers.keySet()) {
            if (!data.toLowerCase().startsWith(name)) {
                continue;
            }

            data = data.substring(name.length());
            if (data.startsWith("_")) {
                data = data.substring(1);
            }

            return replacers.get(name).replace(entity, data);
        }
        return null;
    }

    //

    @Override
    public <D> void addTypeParser(@NotNull Class<D> dataType, @NotNull Function<String, D> parser) {
        parsers.put(dataType, parser);
    }

    //

    public void removeReplacer(@NotNull String name) {
        replacers.remove(name);
    }

    //

    public void addReplacer(@NotNull String name, @NotNull PlaceholderReplacer<T> replacer) {
        replacers.put(name, replacer);
    }

    public <D> void addReplacer(@NotNull String name, @NotNull Class<D> dataType, @NotNull AdvancedPlaceholderReplacer<T, D> replacer) {
        addReplacer(name, (entity, data) -> {
            D parsed = parseData(dataType, data);
            return replacer.replace(entity, parsed);
        });
    }

    //

    private <D> D parseData(Class<D> dataType, String data) {
        Object parsed = null;

        if (parsers.containsKey(dataType)) {
            parsed = parsers.get(dataType).apply(data);
        } else if (STATIC_PARSERS.containsKey(dataType)) {
            parsed = STATIC_PARSERS.get(dataType).apply(data);
        }

        return dataType.cast(parsed);
    }

}