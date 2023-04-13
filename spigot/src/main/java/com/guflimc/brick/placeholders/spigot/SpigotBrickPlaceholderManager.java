package com.guflimc.brick.placeholders.spigot;


import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.common.BrickPlaceholderManager;
import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderManager;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIExpansion;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SpigotBrickPlaceholderManager extends BrickPlaceholderManager<Player> implements SpigotPlaceholderManager, Listener {

    private final SpigotBrickPlaceholders plugin;
    private final Set<PlaceholderAPIExpansion> expansions = new HashSet<>();

    public SpigotBrickPlaceholderManager(SpigotBrickPlaceholders plugin) {
        this.plugin = plugin;

        // plugin enable/disable events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // placeholderapi
        enablePlaceholderAPI();
    }

    // Internal Events

    @Override
    public void register(@NotNull PlaceholderModule<Player> module) {
        super.register(module);
        registerPlacehoderAPI(module);
    }

    @Override
    public void unregister(@NotNull PlaceholderModule<Player> module) {
        super.unregister(module);
        unregisterPlacehoderAPI(module);
    }

    // Bukkit Events

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("PlaceholderAPI")) {
            enablePlaceholderAPI();
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin().getName().equals("PlaceholderAPI")) {
            disablePlaceholderAPI();
        }
    }


    // PlaceholderAPI

    private void registerPlacehoderAPI(PlaceholderModule<Player> module) {
        if ( plugin == null || !plugin.isPlaceholderAPI()) {
            return;
        }

        PlaceholderAPIExpansion expansion = new PlaceholderAPIExpansion(plugin, module);
        expansion.register(); // register placeholders that are registered by this plugin also with PlaceholderAPI
        expansions.add(expansion);
    }

    private void unregisterPlacehoderAPI(PlaceholderModule<Player> module) {
        if ( plugin == null || !plugin.isPlaceholderAPI()) {
            return;
        }

        expansions.stream()
                .filter(expansion -> expansion.getModule().equals(module))
                .findFirst()
                .ifPresent(expansion -> {
                    expansion.unregister();
                    expansions.remove(expansion);
                });
    }

    private void enablePlaceholderAPI() {
        if (!plugin.isPlaceholderAPI()) {
            return;
        }

        plugin.getLogger().info("Hooking into PlaceholderAPI...");
        modules.forEach(SpigotBrickPlaceholderManager.this::registerPlacehoderAPI);

        // use placeholderAPI placeholders through BrickPlaceholders
        addDelegate(new PlaceholderAPIModule());
    }

    private void disablePlaceholderAPI() {
        expansions.clear();
    }
}
