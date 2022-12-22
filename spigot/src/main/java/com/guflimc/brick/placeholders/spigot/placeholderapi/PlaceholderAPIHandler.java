package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.api.extension.PlaceholderExtension;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHandler implements PlaceholderExtension<Player> {

    @Override
    public @NotNull String id() {
        return "papi";
    }

    @Override
    public @Nullable Component replace(@NotNull Player entity, @NotNull String key) {
        return LegacyComponentSerializer.legacySection()
                .deserializeOrNull(PlaceholderAPI.setPlaceholders(entity, "%" + key + "%"));
    }

}
