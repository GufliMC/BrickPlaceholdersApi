package com.guflimc.brick.placeholders.api.resolver;

import com.guflimc.brick.placeholders.api.Converters;
import com.guflimc.brick.placeholders.api.exception.TypeConversionException;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface PlaceholderResolver<E, R> {

    R resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context);

    //

    static <E, R> PlaceholderResolver<E, R> requireEntity(@NotNull PlaceholderResolver<E, R> resolver) {
        return (placeholder, context) -> {
            if (context.entity() == null) {
                return null;
            }
            return resolver.resolve(placeholder, context);
        };
    }

    static <E, R> PlaceholderResolver<E, R> requireViewer(@NotNull PlaceholderResolver<E, R> resolver) {
        return (placeholder, context) -> {
            if (context.viewer() == null) {
                return null;
            }
            return resolver.resolve(placeholder, context);
        };
    }

    //

    static <E, R, T> PlaceholderResolver<E, R> typed(@NotNull Class<T> type,
                                                     @NotNull BiFunction<T, PlaceholderResolveContext<E>, R> resolver) {
        return typed(type, str -> {
            try {
                return Converters.convert(str, type);
            } catch (TypeConversionException e) {
                throw new RuntimeException(e);
            }
        }, resolver);
    }

    static <E, R, T> PlaceholderResolver<E, R> typed(@NotNull Class<T> type, @NotNull Function<String, T> converter,
                                                     @NotNull BiFunction<T, PlaceholderResolveContext<E>, R> resolver) {
        return (placeholder, context) -> {
            T converted;
            try {
                converted = converter.apply(placeholder);
            } catch (Exception ignored) {
                Exception x = new TypeConversionException(String
                        .format("Failed to convert placeholder '%s' to type %s for a typed resolver.",
                                placeholder, type.getSimpleName()));
                throw new RuntimeException(x);
            }
            return resolver.apply(converted, context);
        };
    }

    static <E, R> PlaceholderResolver<E, R> typedInteger(@NotNull BiFunction<Integer, PlaceholderResolveContext<E>, R> resolver) {
        return typed(Integer.class, Integer::parseInt, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedLong(@NotNull BiFunction<Long, PlaceholderResolveContext<E>, R> resolver) {
        return typed(Long.class, Long::parseLong, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedDouble(@NotNull BiFunction<Double, PlaceholderResolveContext<E>, R> resolver) {
        return typed(Double.class, Double::parseDouble, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedFloat(@NotNull BiFunction<Float, PlaceholderResolveContext<E>, R> resolver) {
        return typed(Float.class, Float::parseFloat, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedBoolean(@NotNull BiFunction<Boolean, PlaceholderResolveContext<E>, R> resolver) {
        return typed(Boolean.class, Boolean::parseBoolean, resolver);
    }

    static <E, R, T extends Enum<T>> PlaceholderResolver<E, R> typedEnum(@NotNull Class<T> type,
                                                                         @NotNull BiFunction<T, PlaceholderResolveContext<E>, R> resolver) {
        Function<String, T> converter = s -> Stream.of(type.getEnumConstants())
                .filter(c -> c.name().equalsIgnoreCase(s)).findFirst().orElseThrow();
        return typed(type, converter, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedDecimalFormat(@NotNull BiFunction<DecimalFormat,
            PlaceholderResolveContext<E>, R> resolver) {
        return typed(DecimalFormat.class, DecimalFormat::new, resolver);
    }

    static <E, R> PlaceholderResolver<E, R> typedDateTimeFormatter(@NotNull BiFunction<DateTimeFormatter,
            PlaceholderResolveContext<E>, R> resolver) {
        return typed(DateTimeFormatter.class, DateTimeFormatter::ofPattern, resolver);
    }

}