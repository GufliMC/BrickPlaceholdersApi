package com.guflimc.brick.placeholders.api.module;

import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.TreeSet;

public class BasePlaceholderModule<E> implements PlaceholderModule<E> {

    private final String name;
    private final TreeSet<PlaceholderModule<E>> modules;

    public BasePlaceholderModule(String name) {
        this.name = name;
        modules = new TreeSet<>(Comparator.<PlaceholderModule<E>>comparingInt(m -> m.name().length()).reversed()
                .thenComparing(PlaceholderModule::name));
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    //

    public <R> void register(@NotNull String name, @NotNull PlaceholderResolver<E, R> resolver) {
        modules.add(new PlaceholderModule<E>() {
            @Override
            public @NotNull String name() {
                return name;
            }

            @Override
            public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
                return resolver.resolve(placeholder, context);
            }
        });
    }

    public void register(@NotNull PlaceholderModule<E> module) {
        register(module.name(), module);
    }

    //

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        for (PlaceholderModule<E> module : modules) {
            if (!placeholder.startsWith(module.name())) {
                continue;
            }

            String subpl = placeholder.substring(module.name().length());
            if (subpl.startsWith("_")) {
                subpl = subpl.substring(1);
            }

            return module.resolve(subpl, context);
        }

        return null;
    }

}
