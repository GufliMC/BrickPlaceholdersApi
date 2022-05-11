package com.guflimc.brick.placeholders.spigot;

import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBrickPlaceholders extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        SpigotPlaceholderAPI.registerManager(new SpigotStandardPlaceholderManager());

        // default placeholders
        SpigotPlaceholderAPI.get().registerReplacer("username", p -> Component.text(p.getName()));
        SpigotPlaceholderAPI.get().registerReplacer("displayname", p ->
                LegacyComponentSerializer.legacySection().deserialize(p.getDisplayName()));

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
