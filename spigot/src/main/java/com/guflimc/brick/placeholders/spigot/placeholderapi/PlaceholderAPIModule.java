package com.guflimc.brick.placeholders.spigot.placeholderapi;

import com.guflimc.brick.placeholders.api.Converters;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIModule implements PlaceholderModule<Player> {

    @Override
    public @NotNull String name() {
        return "papi"; // not important, is never used
    }

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<Player> context) {
        String result = "%" + placeholder + "%";
        if (context.entity() != null && context.viewer() != null) {
            result = PlaceholderAPI.setRelationalPlaceholders(context.entity(), context.viewer(), result);
        }
        result = PlaceholderAPI.setPlaceholders(context.entity(), result);
        return Converters.deserialize(result);
    }

}
