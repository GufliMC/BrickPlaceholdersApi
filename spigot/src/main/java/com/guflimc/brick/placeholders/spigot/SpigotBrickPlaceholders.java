package com.guflimc.brick.placeholders.spigot;

import com.guflimc.brick.placeholders.api.extension.AdvancedPlaceholderExtension;
import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIExpansion;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBrickPlaceholders extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        SpigotBrickPlaceholderManager manager = new SpigotBrickPlaceholderManager();
        SpigotPlaceholderAPI.registerManager(manager);

        // default placeholders
        // default placeholders
        AdvancedPlaceholderExtension<Player> ext = SpigotPlaceholderAPI.get().registerExtension("player");
        ext.addReplacer("username", (p, d) -> Component.text(p.getName()));
        ext.addReplacer("displayname", (p, d) -> LegacyComponentSerializer.legacySection().deserialize(p.getDisplayName()));

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Hooked into PlaceholderAPI.");
            new PlaceholderAPIExpansion(this).register(); // brick placeholders in placeholder api
            manager.registerExtension(new PlaceholderAPIHandler()); // placeholder api placeholders in brick
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
