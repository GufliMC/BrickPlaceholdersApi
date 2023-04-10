package com.guflimc.brick.placeholdeders.common.tests;

import com.guflimc.brick.placeholdeders.common.tests.model.Entity;
import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.ReplacementConversionException;
import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperatorPlaceholderTests {

    private static PlaceholderManager<Entity> manager;

    @BeforeAll
    public static void init() {
        manager = new BrickPlaceholderManager<>();

        BasePlaceholderModule<Entity> module = new BasePlaceholderModule<>("entity");
        module.register("name", (placeholder, context) -> context.entity().name());
        module.register("name_display", (placeholder, context) ->
                Component.text(context.entity().name(), NamedTextColor.RED));
        manager.register(module);
    }

    @Test
    public void mapTest() throws ReplacementConversionException {
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
    public void ifPresentTest() throws ReplacementConversionException {
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
