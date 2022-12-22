package com.guflimc.brick.placeholdeders.common.tests.model;

import com.guflimc.brick.placeholders.api.extension.PlaceholderExtension;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityNameExtension implements PlaceholderExtension<Entity> {

    @Override
    public @NotNull String id() {
        return "entity_name";
    }

    @Override
    public @Nullable Component replace(@NotNull Entity entity, String data) {
        return Component.text(entity.name());
    }

}
