package com.guflimc.brick.placeholders.minestom.api;

public class MinestomPlaceholderAPI {

    private static MinestomPlaceholderManager placeholderManager;

    public static void registerManager(MinestomPlaceholderManager manager) {
        placeholderManager = manager;
    }

    //

    public static MinestomPlaceholderManager get() {
        return placeholderManager;
    }

}
