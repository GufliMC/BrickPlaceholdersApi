package com.guflimc.brick.placeholders.spigot;

import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIExpansion;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBrickPlaceholders extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        SpigotBrickPlaceholderManager manager = new SpigotBrickPlaceholderManager();
        SpigotPlaceholderAPI.registerManager(manager);

        // default placeholders
        SpigotPlaceholderAPI.get().registerReplacer("username", p -> Component.text(p.getName()));
        SpigotPlaceholderAPI.get().registerReplacer("displayname", p ->
                LegacyComponentSerializer.legacySection().deserialize(p.getDisplayName()));

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Hooked into PlaceholderAPI.");
            new PlaceholderAPIExpansion(this).register(); // brick placeholders in placeholder api
            manager.registerHandler(new PlaceholderAPIHandler()); // placeholder api placeholders in brick
        }

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getName() + " v" + getDescription().getVersion();
    }

}
