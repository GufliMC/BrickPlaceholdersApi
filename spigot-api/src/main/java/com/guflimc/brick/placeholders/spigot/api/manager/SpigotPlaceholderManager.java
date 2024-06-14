package com.guflimc.brick.placeholders.spigot.api.manager;

import com.guflimc.brick.placeholders.api.manager.PlaceholderManager;
import com.guflimc.brick.placeholders.api.manager.PlaceholderModuleManager;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.spigot.api.hooks.PlaceholderPluginHook;
import com.guflimc.brick.placeholders.spigot.api.hooks.placeholderapi.PlaceholderAPIDetector;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpigotPlaceholderManager implements PlaceholderManager<Player> {

    private final PlaceholderModuleManager<Player> proxied = new PlaceholderModuleManager<>();;

    private final List<PlaceholderPluginHook> hooks = new ArrayList<>();

    public SpigotPlaceholderManager(JavaPlugin plugin) {
        new PlaceholderAPIDetector(plugin, this::enable, this::disable);
    }

    private void enable(PlaceholderPluginHook hook) {
        hooks.add(hook);
        proxied.modules().forEach(hook::register);
    }

    private void disable(PlaceholderPluginHook hook) {
        hooks.remove(hook);
        proxied.modules().forEach(hook::unregister);
    }

    @Override
    public Component replace(@NotNull Component component, @NotNull PlaceholderResolveContext<Player> context) {
        return proxied.replace(component, context);
    }

    @Override
    public Component replace(@NotNull String text, @NotNull PlaceholderResolveContext<Player> context) {
        return proxied.replace(text, context);
    }

    @Override
    public @Nullable Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<Player> context) {
        Component resolved = proxied.resolve(placeholder, context);
        if (resolved != null) {
            return resolved;
        }

        return hooks.stream()
                .map(hook -> hook.resolve(placeholder, context))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void register(@NotNull PlaceholderModule<Player> module) {
        proxied.register(module);
        hooks.forEach(hook -> hook.register(module));
    }

    @Override
    public void unregister(@NotNull PlaceholderModule<Player> module) {
        proxied.unregister(module);
        hooks.forEach(hook -> hook.unregister(module));
    }

}
