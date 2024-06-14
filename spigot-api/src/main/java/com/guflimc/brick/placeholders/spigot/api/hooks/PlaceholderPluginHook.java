package com.guflimc.brick.placeholders.spigot.api.hooks;

import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import com.guflimc.brick.placeholders.spigot.api.hooks.placeholderapi.PlaceholderAPIExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlaceholderPluginHook implements PlaceholderResolver<Player> {

    public abstract void register(@NotNull PlaceholderModule<Player> module);

    public abstract void unregister(@NotNull PlaceholderModule<Player> module);

}
