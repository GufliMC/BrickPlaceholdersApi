package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;
    private final PlaceholderModule<Player> module;

    public PlaceholderAPIExpansion(JavaPlugin plugin, PlaceholderModule<Player> module) {
        this.plugin = plugin;
        this.module = module;
    }

    public PlaceholderModule<Player> getModule() {
        return module;
    }

    @Override
    public @NotNull String getIdentifier() {
        return module.name();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    //

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        Object replacement = module.resolve(params, PlaceholderResolveContext.of(player));
        if (replacement == null) {
            return null;
        }

        if (replacement instanceof Component result) {
            return LegacyComponentSerializer.legacySection()
                    .serialize(result);
        }

        return replacement.toString();
    }
}
