package com.guflimc.brick.placeholders.minestom.api;

import org.jetbrains.annotations.ApiStatus;

public class MinestomPlaceholderAPI {

    private static MinestomPlaceholderManager placeholderManager;

    @ApiStatus.Internal
    public static void registerManager(MinestomPlaceholderManager manager) {
        placeholderManager = manager;
    }

    //

    /**
     * Get the registered placeholder manager.
     * @return the registered placeholder manager
     */
    public static MinestomPlaceholderManager get() {
        return placeholderManager;
    }

}
