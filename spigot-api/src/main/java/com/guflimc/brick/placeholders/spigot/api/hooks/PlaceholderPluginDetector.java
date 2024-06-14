package com.guflimc.brick.placeholders.spigot.api.hooks;

import java.util.function.Consumer;

public abstract class PlaceholderPluginDetector {

    private final Consumer<PlaceholderPluginHook> enable;
    private final Consumer<PlaceholderPluginHook> disable;

    protected PlaceholderPluginDetector(Consumer<PlaceholderPluginHook> enable, Consumer<PlaceholderPluginHook> disable) {
        this.enable = enable;
        this.disable = disable;
    }

    protected final void enable(PlaceholderPluginHook hook) {
        enable.accept(hook);
    }

    protected void disable(PlaceholderPluginHook hook) {
        disable.accept(hook);
    }

}
