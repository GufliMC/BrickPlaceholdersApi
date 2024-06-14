package com.guflimc.brick.placeholders.spigot.api.hooks.placeholderapi;

import com.guflimc.adventure.MixedLegacyComponentSerializer;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.spigot.api.hooks.PlaceholderPluginHook;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PlaceholderAPIHook extends PlaceholderPluginHook {

    private final JavaPlugin plugin;
    private final Set<PlaceholderAPIExpansion> expansions = new HashSet<>();

    public PlaceholderAPIHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<Player> context) {
        String result = "%" + placeholder + "%";
        if (context.entity() != null && context.viewer() != null) {
            result = PlaceholderAPI.setRelationalPlaceholders(context.entity(), context.viewer(), result);
        }
        result = PlaceholderAPI.setPlaceholders(context.entity(), result);
        return MixedLegacyComponentSerializer.deserialize(result);
    }

    @Override
    public void register(@NotNull PlaceholderModule<Player> module) {
        PlaceholderAPIExpansion expansion = new PlaceholderAPIExpansion(plugin, module);
        expansion.register();
        expansions.add(expansion);
    }

    @Override
    public void unregister(@NotNull PlaceholderModule<Player> module) {
        expansions.stream()
                .filter(expansion -> expansion.getModule().equals(module))
                .findFirst()
                .ifPresent(expansion -> {
                    expansion.unregister();
                    expansions.remove(expansion);
                });
    }

}
