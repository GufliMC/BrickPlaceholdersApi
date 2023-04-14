package com.guflimc.brick.placeholders.api;

import com.guflimc.brick.placeholders.api.exception.TypeConversionException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Converters {

    private record Key(Class<?> from, Class<?> to) {
    }

    private static final Map<Key, Function<?, ?>> CONVERTERS = new HashMap<>();

    static {
        // object -> ...
        register(Object.class, String.class, Object::toString);
        register(Object.class, Component.class, o -> deserialize(o.toString()));

        // string -> ...
        register(String.class, String.class, s -> s);
        register(String.class, Integer.class, Integer::parseInt);
        register(String.class, Long.class, Long::parseLong);
        register(String.class, Float.class, Float::parseFloat);
        register(String.class, Double.class, Double::parseDouble);
        register(String.class, Boolean.class, Boolean::parseBoolean);
        register(String.class, Byte.class, Byte::parseByte);
        register(String.class, Short.class, Short::parseShort);
        register(String.class, Character.class, s -> s.charAt(0));
        register(String.class, Component.class, Component::text);
        register(String.class, Instant.class, Instant::parse);
        register(String.class, LocalTime.class, LocalTime::parse);
        register(String.class, LocalDate.class, LocalDate::parse);
        register(String.class, TimeZone.class, TimeZone::getTimeZone);
        register(String.class, DecimalFormat.class, DecimalFormat::new);
        register(String.class, DateTimeFormatter.class, DateTimeFormatter::ofPattern);

        register(String.class, int.class, Integer::parseInt);
        register(String.class, long.class, Long::parseLong);
        register(String.class, float.class, Float::parseFloat);
        register(String.class, double.class, Double::parseDouble);
        register(String.class, boolean.class, Boolean::parseBoolean);
        register(String.class, byte.class, Byte::parseByte);
        register(String.class, short.class, Short::parseShort);
        register(String.class, char.class, s -> s.charAt(0));

        // component -> ...
        register(Component.class, String.class, LegacyComponentSerializer.legacySection()::serialize);

        // unboxed primitives -> boxed
        register(int.class, Integer.class, Integer::valueOf);
        register(long.class, Long.class, Long::valueOf);
        register(float.class, Float.class, Float::valueOf);
        register(double.class, Double.class, Double::valueOf);
        register(boolean.class, Boolean.class, Boolean::valueOf);
        register(byte.class, Byte.class, Byte::valueOf);
        register(short.class, Short.class, Short::valueOf);
        register(char.class, Character.class, Character::valueOf);

        // boxed primitives -> unboxed
        register(Integer.class, int.class, Integer::intValue);
        register(Long.class, long.class, Long::longValue);
        register(Float.class, float.class, Float::floatValue);
        register(Double.class, double.class, Double::doubleValue);
        register(Boolean.class, boolean.class, Boolean::booleanValue);
        register(Byte.class, byte.class, Byte::byteValue);
        register(Short.class, short.class, Short::shortValue);
        register(Character.class, char.class, Character::charValue);

        // numbers
        register(Number.class, Integer.class, Number::intValue);
        register(Number.class, Long.class, Number::longValue);
        register(Number.class, Float.class, Number::floatValue);
        register(Number.class, Double.class, Number::doubleValue);
        register(Number.class, Byte.class, Number::byteValue);
        register(Number.class, Short.class, Number::shortValue);
    }

    private Converters() {
    }

    public static <F, T> void register(Class<F> from, Class<T> to, Function<F, T> converter) {
        Key key = new Key(from, to);
        CONVERTERS.put(key, converter);
    }

    public static <T> T convert(@Nullable Object obj, @NotNull Class<T> to) throws TypeConversionException {
        // null -> null
        if (obj == null) {
            return null;
        }

        // T -> T
        if ( to.isInstance(obj) ) {
            return to.cast(obj);
        }

        // string -> enum
        if (to.isEnum()) {
            Function<String, ?> converter = s -> Stream.of(to.getEnumConstants())
                    .map(c -> (Enum<?>) c)
                    .filter(c -> c.name().equalsIgnoreCase(s)).findFirst().orElse(null);
            return to.cast(converter.apply(obj.toString()));
        }

        try {
            return _convert(obj, to);
        } catch (Exception e) {
            throw new TypeConversionException(String.format("Failed to convert object of type %s to type %s.",
                    obj.getClass().getSimpleName(), to.getSimpleName()), e);
        }
    }


    private static <T> T _convert(@NotNull Object obj, @NotNull Class<T> to) {
        Set<Class<?>> visited = new HashSet<>();
        Queue<Class<?>> queue = new ArrayDeque<>();

        queue.add(obj.getClass());

        do {
            Class<?> current = queue.remove();
            if ( visited.contains(current) ) {
                continue;
            }

            visited.add(current);
            Function<Object, T> converter = converter(new Key(current, to));
            if ( converter != null ) {
                return converter.apply(obj);
            }

            queue.addAll(Arrays.asList(current.getInterfaces()));
            if ( current.getSuperclass() != null ) {
                queue.add(current.getSuperclass());
            }
        } while ( !queue.isEmpty() );

        throw new IllegalArgumentException(String.format("No converter found for %s -> %s",
                obj.getClass().getSimpleName(), to.getSimpleName()));
    }

    private static <T> Function<Object, T> converter(@NotNull Key key) {
        if ( !CONVERTERS.containsKey(key) ) {
            return null;
        }
        return (Function<Object, T>) CONVERTERS.get(key);
    }

    private final static LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component deserialize(String input) {
        if (input == null) {
            return null;
        }

        return serializer.deserialize(input);
    }
}
