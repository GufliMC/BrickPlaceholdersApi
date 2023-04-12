package com.guflimc.brick.placeholdeders.common.tests;

import com.guflimc.brick.placeholdeders.common.tests.model.Entity;
import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.TypeConversionException;
import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConversionTests {

    private PlaceholderManager<Entity> manager;
    private Object value;

    @BeforeEach
    public void init() {
        manager = new BrickPlaceholderManager<>();

        BasePlaceholderModule<Entity> module = new BasePlaceholderModule<>("fixed");
        module.register("", (placeholder, context) -> value);
        manager.register(module);
    }

    @Test
    public void convertTest() throws TypeConversionException {
        value = Math.random();
        assertEquals(value, manager.resolve("fixed", PlaceholderResolveContext.of(), Double.class));
        assertEquals(value, manager.resolve("fixed", PlaceholderResolveContext.of(), double.class));
        assertEquals(value + "", manager.resolve("fixed", PlaceholderResolveContext.of(), String.class));

        value = "true";
        assertEquals(true, manager.resolve("fixed", PlaceholderResolveContext.of(), boolean.class));
        assertEquals(true, manager.resolve("fixed", PlaceholderResolveContext.of(), Boolean.class));
        assertEquals("true", manager.resolve("fixed", PlaceholderResolveContext.of(), String.class));
        assertEquals(Component.text(true), manager.resolve("fixed", PlaceholderResolveContext.of(), Component.class));
        assertEquals(value + "", manager.resolve("fixed", PlaceholderResolveContext.of(), String.class));

        value = Component.text("ola ola", NamedTextColor.GREEN);
        assertEquals(value, manager.resolve("fixed", PlaceholderResolveContext.of(), Component.class));
        assertEquals("§aola ola", manager.resolve("fixed", PlaceholderResolveContext.of(), String.class));
    }


}
