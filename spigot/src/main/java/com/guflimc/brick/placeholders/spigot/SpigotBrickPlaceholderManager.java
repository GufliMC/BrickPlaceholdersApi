package com.guflimc.brick.placeholders.spigot;


import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderManager;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIExpansion;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class SpigotBrickPlaceholderManager extends BrickPlaceholderManager<Player> implements SpigotPlaceholderManager {

    private final SpigotBrickPlaceholders plugin;
    private final Set<PlaceholderAPIExpansion> expansions = new HashSet<>();

    public SpigotBrickPlaceholderManager(SpigotBrickPlaceholders plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void onRegister(PlaceholderModule<Player> module) {
        if (plugin.isPlaceholderAPI()) {
            PlaceholderAPIExpansion expansion = new PlaceholderAPIExpansion(plugin, module);
            expansion.register(); // register placeholders that are registered by this plugin also with PlaceholderAPI
            expansions.add(expansion);
        }
    }

    @Override
    protected void onUnregister(PlaceholderModule<Player> module) {
        if (plugin.isPlaceholderAPI()) {
            expansions.stream()
                    .filter(expansion -> expansion.getModule().equals(module))
                    .findFirst()
                    .ifPresent(expansion -> {
                        expansion.unregister();
                        expansions.remove(expansion);
                    });
        }
    }
}
