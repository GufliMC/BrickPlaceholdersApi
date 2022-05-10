package com.guflimc.brick.placeholders.spigot.api;

public class SpigotPlaceholderAPI {

    private static SpigotPlaceholderManager placeholderManager;

    public static void registerManager(SpigotPlaceholderManager manager) {
        placeholderManager = manager;
    }

    //

    public static SpigotPlaceholderManager get() {
        return placeholderManager;
    }

}
