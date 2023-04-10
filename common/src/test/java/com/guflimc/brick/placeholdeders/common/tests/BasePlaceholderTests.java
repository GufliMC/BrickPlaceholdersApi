package com.guflimc.brick.placeholdeders.common.tests;

import com.guflimc.brick.placeholdeders.common.tests.model.Entity;
import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasePlaceholderTests {

    private static Entity entity;
    private static PlaceholderManager<Entity> manager;

    @BeforeAll
    public static void init() {
        manager = new BrickPlaceholderManager<>();
        entity = new Entity("Eric");
    }

    @Test
    public void standardTest() {
        BasePlaceholderModule<Entity> module = new BasePlaceholderModule<>("entity");
        module.register("name", (placeholder, context) -> context.entity().name());
        manager.register(module);

        Component msg = manager.replace("Hello %entity_name%!", entity);
        String result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Hello Eric!", result);
    }

    @Test
    public void advancedTest() {
        BasePlaceholderModule<Entity> module = new BasePlaceholderModule<>("advanced");
        module.register("today", PlaceholderResolver.typedDateTimeFormatter((p, ctx) -> p.format(LocalDateTime.now())));
        module.register("balance", PlaceholderResolver.typedDecimalFormat((p, ctx) -> p.format(69.420)));
        manager.register(module);


        Component msg = manager.replace("Today is %advanced_today_yyyy-dd%!", PlaceholderResolveContext.of());
        String result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Today is " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-dd")) + "!", result);

        msg = manager.replace("My balance is %advanced_balance_000.0000%!", PlaceholderResolveContext.of());
        result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("My balance is 069.4200!", result);

        msg = manager.replace("My balance is %advanced_balance_#.#%!", PlaceholderResolveContext.of());
        result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("My balance is 69.4!", result);
    }
}
