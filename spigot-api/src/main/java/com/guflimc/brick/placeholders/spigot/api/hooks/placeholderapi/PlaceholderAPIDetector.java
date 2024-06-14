package com.guflimc.brick.placeholders.spigot.api.hooks.placeholderapi;

import com.guflimc.brick.placeholders.spigot.api.hooks.PlaceholderPluginDetector;
import com.guflimc.brick.placeholders.spigot.api.hooks.PlaceholderPluginHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class PlaceholderAPIDetector extends PlaceholderPluginDetector implements Listener {

    private final JavaPlugin plugin;
    private PlaceholderPluginHook hook;

    public PlaceholderAPIDetector(JavaPlugin plugin, Consumer<PlaceholderPluginHook> enable, Consumer<PlaceholderPluginHook> disable) {
        super(enable, disable);
        this.plugin = plugin;

        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(this, plugin);

        if ( pm.isPluginEnabled("PlaceholderAPI") ) {
            enable();
        }
    }

    private void enable() {
        hook = new PlaceholderAPIHook(plugin);
        super.enable(hook);
    }

    private void disable() {
        super.disable(hook);
        hook = null;
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("PlaceholderAPI")) {
            enable();
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin().getName().equals("PlaceholderAPI")) {
            disable();
        }
    }

}
