package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public PlaceholderAPIExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "brick";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    //

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if ( player == null ) {
            return null;
        }
        Component replacement = SpigotPlaceholderAPI.get().replacement(player, params);
        if ( replacement == null ) {
            return null;
        }
        return LegacyComponentSerializer.legacySection()
                .serialize(replacement);
    }
}
