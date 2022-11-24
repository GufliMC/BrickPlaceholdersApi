package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.common.handler.PlaceholderHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class PlaceholderAPIHandler implements PlaceholderHandler<Player> {

    private final static Pattern PATTERN = Pattern.compile("(%[^%]+%)");

    @Override
    public @NotNull Pattern pattern() {
        return PATTERN;
    }

    @Override
    public @Nullable Component replace(@NotNull Player entity, @NotNull String key) {
        return LegacyComponentSerializer.legacySection()
                .deserializeOrNull(PlaceholderAPI.setPlaceholders(entity, "%" + key + "%"));
    }
}
