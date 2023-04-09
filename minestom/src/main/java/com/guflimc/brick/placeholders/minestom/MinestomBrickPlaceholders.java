package com.guflimc.brick.placeholders.minestom;

import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import com.guflimc.brick.placeholders.minestom.api.MinestomPlaceholderAPI;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;

public class MinestomBrickPlaceholders extends Extension {

    private MinestomBrickPlaceholderManager manager;

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        MinestomPlaceholderAPI.registerManager(new MinestomBrickPlaceholderManager());

        // built-in placeholder modules
        player();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

    public void player() {
        BasePlaceholderModule<Player> module = new BasePlaceholderModule<>("player");
        module.register("name", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getName()));
        module.register("displayname", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getDisplayName()));
        module.register("health", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().getHealth())));
        module.register("food", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().getFood())));
        module.register("x", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().getPosition().x())));
        module.register("y", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().getPosition().y())));
        module.register("z", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().getPosition().z())));
        module.register("location", PlaceholderResolver.requireEntity((p, ctx) -> String.format("%s, %s, %s",
                ctx.entity().getPosition().x(), ctx.entity().getPosition().y(), ctx.entity().getPosition().z())));
        module.register("is_flying", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().isFlying())));
        module.register("is_sneaking", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().isSneaking())));
        module.register("is_sprinting", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().isSprinting())));
        module.register("is_invisible", PlaceholderResolver.requireEntity((p, ctx) -> String.valueOf(ctx.entity().isInvisible())));
        manager.register(module);
    }
}
