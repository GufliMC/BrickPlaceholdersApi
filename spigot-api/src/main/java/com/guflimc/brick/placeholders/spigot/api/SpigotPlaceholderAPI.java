package com.guflimc.brick.placeholders.spigot.api;

import org.jetbrains.annotations.ApiStatus;

public class SpigotPlaceholderAPI {

    private static SpigotPlaceholderManager placeholderManager;

    @ApiStatus.Internal
    public static void registerManager(SpigotPlaceholderManager manager) {
        placeholderManager = manager;
    }

    //

    /**
     * Get the registered placeholder manager.
     * @return the registered placeholder manager
     */
    public static SpigotPlaceholderManager get() {
        return placeholderManager;
    }

}
