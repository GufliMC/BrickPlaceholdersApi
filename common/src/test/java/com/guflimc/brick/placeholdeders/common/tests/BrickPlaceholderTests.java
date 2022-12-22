package com.guflimc.brick.placeholdeders.common.tests;

import com.guflimc.brick.placeholdeders.common.tests.model.Entity;
import com.guflimc.brick.placeholdeders.common.tests.model.EntityNameExtension;
import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderExtension;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrickPlaceholderTests {

    private static Entity entity;
    private static PlaceholderManager<Entity> manager;

    @BeforeAll
    public static void init() {
        manager = new BrickPlaceholderManager<>();
        entity = new Entity("Berta");
    }

    @Test
    public void standardTest() {
        manager.registerExtension(new EntityNameExtension());

        Component msg = manager.replace(entity, "Hello {entity_name}!");
        String result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Hello Berta!", result);
    }

    @Test
    public void advancedTest() {
        AdvancedPlaceholderExtension<Entity> ext = manager.registerExtension("party");

        ext.addReplacer("today", DateTimeFormatter.class, (e, f) -> Component.text(f.format(LocalDateTime.now())));
        ext.addReplacer("balance", DecimalFormat.class, (e, f) -> Component.text(f.format(69.420)));

        Component msg = manager.replace(entity, "Test {party_today_yyyy-dd} xx!");
        String result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Test " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-dd")) + " xx!", result);

        msg = manager.replace(entity, "Party {party_balance_000.0000}!");
        result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Party 069.4200!", result);

        msg = manager.replace(entity, "Party {party_balance_#.#}!");
        result = PlainTextComponentSerializer.plainText().serialize(msg);
        assertEquals("Party 69.4!", result);
    }
}
