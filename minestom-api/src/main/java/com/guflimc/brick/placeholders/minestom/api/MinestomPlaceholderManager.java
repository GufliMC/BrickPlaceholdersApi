package com.guflimc.brick.placeholders.minestom.api;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface MinestomPlaceholderManager extends PlaceholderManager<Player> {

    @Override
    Component replace(@NotNull Player entity, @NotNull Component component);

    @Override
    Component replace(@NotNull Player entity, @NotNull String text);

    @Override
    Collection<String> placeholders();

    @Override
    void registerReplacer(@NotNull String key, @NotNull BiFunction<Player, String, Component> replacer);

    @Override
    void registerReplacer(@NotNull String key, @NotNull Function<Player, Component> replacer);

    @Override
    void unregisterReplacer(@NotNull String key);
}
