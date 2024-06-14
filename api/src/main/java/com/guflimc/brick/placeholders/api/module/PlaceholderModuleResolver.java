package com.guflimc.brick.placeholders.api.module;

import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolver;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class PlaceholderModuleResolver<E> implements PlaceholderResolver<E> {

    private final TreeSet<PlaceholderModule<E>> modules = new TreeSet<>(
            Comparator
                    .<PlaceholderModule<E>>comparingInt(m -> m.name().length())
                    .reversed()
                    .thenComparing(PlaceholderModule::name)
    );

    public Set<PlaceholderModule<E>> modules() {
        return Collections.unmodifiableSet(modules);
    }

    public <R> void register(@NotNull String name, @NotNull PlaceholderResolver<E> resolver) {
        register(new PlaceholderModule<E>() {
            @Override
            public @NotNull String name() {
                return name;
            }

            @Override
            public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
                return resolver.resolve(placeholder, context);
            }
        });
    }

    public void register(@NotNull PlaceholderModule<E> module) {
        if (!module.name().matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException("The module name may only contain lowercase letters, numbers and underscores.");
        }
        if (modules.stream().anyMatch(m -> module.name().equals(m.name()))) {
            throw new IllegalArgumentException("A module with that name is already registered.");
        }
        modules.add(module);
    }

    public void unregister(@NotNull PlaceholderModule<E> module) {
        modules.remove(module);
    }

    @Override
    public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
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
