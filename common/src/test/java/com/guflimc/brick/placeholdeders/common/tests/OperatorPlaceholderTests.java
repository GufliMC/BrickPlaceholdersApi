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
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperatorPlaceholderTests {

    private PlaceholderManager<Entity> manager;
    private Object value;

    @BeforeEach
    public void init() {
        manager = new BrickPlaceholderManager<>();

        BasePlaceholderModule<Entity> module = new BasePlaceholderModule<>("entity");
        module.register("name", (placeholder, context) -> context.entity().name());
        module.register("name_display", (placeholder, context) ->
                Component.text(context.entity().name(), NamedTextColor.RED));
        manager.register(module);

        module = new BasePlaceholderModule<>("fixed");
        module.register("", (placeholder, context) -> value);
        manager.register(module);
    }

    @Test
    public void mapTest() throws TypeConversionException {
        // text
        String placeholder = "op_map_\"entity_name\"_red:kitty_eric:laurie";
        assertEquals("kitty", manager.resolve(placeholder, new Entity("red"), String.class));
        assertEquals("laurie", manager.resolve(placeholder, new Entity("eric"), String.class));
        assertEquals("fez", manager.resolve(placeholder, new Entity("fez"), String.class));

        placeholder = "op_map_\"entity_name\"_red:kitty_eric:\"laurie foreman\"_\"{} foreman\"";
        assertEquals("laurie foreman", manager.resolve(placeholder, new Entity("eric"), String.class));
        assertEquals("kitty foreman", manager.resolve(placeholder, new Entity("kitty"), String.class));

        // component
        placeholder = "op_map_\"entity_name_display\"_&cred:&akitty";
        assertEquals(Component.text("kitty", NamedTextColor.GREEN),
                manager.resolve(placeholder, new Entity("red"), Component.class));
    }

    @Test
    public void mapRange() throws TypeConversionException {
        // text
        String placeholder = "op_map_range_\"fixed\"_0-0.5:\"Bad\"_0.5-0.75:\"Ok\"_0.75-0.9:\"Good\"_0.9-1:\"Excellent\"";
        value = 0;
        assertEquals("Bad", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 0.3;
        assertEquals("Bad", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 0.5;
        assertEquals("Ok", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 0.75;
        assertEquals("Good", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 1;
        assertEquals("Excellent", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 2;
        assertEquals("2.0", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));

        placeholder = "op_map_range_\"fixed\"_<10:\"Small\"_>10:\"Big\"";
        value = 10f;
        assertEquals("Big", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 420;
        assertEquals("Big", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = 0.6d;
        assertEquals("Small", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
        value = -69L;
        assertEquals("Small", manager.resolve(placeholder, PlaceholderResolveContext.of(), String.class));
    }

    @Test
    public void ifPresentTest() throws TypeConversionException {
        // text
        String placeholder = "op_if_present_\"entity_name\"_[{}]_[empty]";
        assertEquals("[donna]", manager.resolve(placeholder, new Entity("donna"), String.class));
        assertEquals("[empty]", manager.resolve(placeholder, new Entity(null), String.class));

        placeholder = "op_if_present_\"entity_name\"_\"'{}'\"";
        assertEquals("'jackie'", manager.resolve(placeholder, new Entity("jackie"), String.class));
        assertNull(manager.resolve(placeholder, new Entity(null), String.class));

        // component
        placeholder = "op_if_present_\"entity_name_display\"_&amichael";
        assertEquals(Component.text("michael", NamedTextColor.GREEN),
                manager.resolve(placeholder, new Entity("steven"), Component.class));
    }

}
