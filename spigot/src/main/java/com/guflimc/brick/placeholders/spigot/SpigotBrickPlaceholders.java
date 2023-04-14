package com.guflimc.brick.placeholders.spigot;

import com.guflimc.brick.placeholders.api.module.BasePlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import com.guflimc.brick.placeholders.common.PlaceholderConfig;
import com.guflimc.brick.placeholders.common.modules.OperatorIfPresentPlaceholderModule;
import com.guflimc.brick.placeholders.common.modules.OperatorMapPlaceholderModule;
import com.guflimc.brick.placeholders.common.modules.OperatorMapRangePlaceholderModule;
import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import com.guflimc.brick.placeholders.spigot.placeholderapi.PlaceholderAPIModule;
import com.guflimc.config.toml.TomlConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBrickPlaceholders extends JavaPlugin {

    private PlaceholderConfig config;
    private SpigotBrickPlaceholderManager manager;

    @Override
    public void onEnable() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        // config
        config = TomlConfig.load(getDataFolder().toPath().resolve("config.toml"), new PlaceholderConfig());

        // manager
        manager = new SpigotBrickPlaceholderManager(this);
        SpigotPlaceholderAPI.registerManager(manager);

        // delegate to other placeholder plugins
        placeholderapi();

        // built-in placeholder modules
        modules();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getName() + " v" + getDescription().getVersion();
    }

    //

    public boolean isPlaceholderAPI() {
        return getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    //

    public void placeholderapi() {
        if (!isPlaceholderAPI()) {
            return;
        }

        getLogger().info("Hooking into PlaceholderAPI...");

        // use placeholderAPI placeholders through BrickPlaceholders
        manager.addDelegate(new PlaceholderAPIModule());
    }

    public void modules() {
        if (config.modules == null) {
            return;
        }

        if (config.modules.player) {
            player();
        }

        if (config.modules.operator) {
            operator();
        }
    }

    private void player() {
        BasePlaceholderModule<Player> module = new BasePlaceholderModule<>("player");
        module.register("name", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getName()));
        module.register("displayname", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getDisplayName()));
        module.register("health", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getHealth()));
        module.register("level", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getLevel()));
        module.register("exp", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getExp()));
        module.register("food", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().getFoodLevel()));
        module.register("is_flying", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isFlying()));
        module.register("is_sneaking", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isSneaking()));
        module.register("is_sprinting", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isSprinting()));
        module.register("is_op", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isOp()));
        module.register("is_invisible", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isInvisible()));
        module.register("is_invulnerable", PlaceholderResolver.requireEntity((p, ctx) -> ctx.entity().isInvulnerable()));
        manager.register(module);
    }

    private void operator() {
        manager.register(new OperatorIfPresentPlaceholderModule<>(manager));
        manager.register(new OperatorMapPlaceholderModule<>(manager));
        manager.register(new OperatorMapRangePlaceholderModule<>(manager));
    }


}
