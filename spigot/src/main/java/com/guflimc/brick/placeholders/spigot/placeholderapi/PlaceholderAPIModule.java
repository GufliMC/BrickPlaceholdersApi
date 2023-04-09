package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIModule implements PlaceholderModule<Player> {

    @Override
    public @NotNull String name() {
        return "papi"; // not important, is never used
    }

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<Player> context) {
        return LegacyComponentSerializer.legacySection()
                .deserializeOrNull(PlaceholderAPI.setPlaceholders(context.entity(), "%" + placeholder + "%"));
    }
}
