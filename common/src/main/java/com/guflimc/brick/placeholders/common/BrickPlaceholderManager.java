package com.guflimc.brick.placeholders.common;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.exception.ReplacementConversionException;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import com.guflimc.brick.placeholders.common.modules.OperatorPlaceholderModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class BrickPlaceholderManager<E> implements PlaceholderManager<E> {

    private final static Logger LOGGER = LoggerFactory.getLogger(BrickPlaceholderManager.class);

    private final static Pattern PATTERN = Pattern.compile("(%[^%]+%)");

    protected final TreeSet<PlaceholderModule<E>> modules;
    private final List<PlaceholderModule<E>> delegates = new CopyOnWriteArrayList<>();

    public BrickPlaceholderManager() {
        modules = new TreeSet<>(Comparator.<PlaceholderModule<E>>comparingInt(m -> m.name().length()).reversed()
                .thenComparing(PlaceholderModule::name));

        // default modules
        register(new OperatorPlaceholderModule<>(this));
    }

    //

    @Override
    public Component replace(@NotNull Component component, @NotNull PlaceholderResolveContext<E> context) {
        return component.replaceText(builder -> builder.match(PATTERN).replacement((mr, tb) -> replace(mr, context)));
    }

    @Override
    public Component replace(@NotNull String text, @NotNull PlaceholderResolveContext<E> context) {
        return replace(Component.text(text), context);
    }

    private ComponentLike replace(@NotNull MatchResult match, @NotNull PlaceholderResolveContext<E> context) {
        String group = match.group().toLowerCase();
        String placeholder = group.substring(1, group.length() - 1); // remove surrounding brackets
        try {
            Component replacement = resolve(placeholder, context, Component.class);
            if ( replacement != null ) {
                return replacement;
            }
        } catch (ReplacementConversionException ex) {
            LOGGER.error(ex.getMessage());
        }
        return Component.text(group);
    }

    //

    @Override
    public Object resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        for ( PlaceholderModule<E> module : modules ) {
            if (!placeholder.startsWith(module.name())) {
                continue;
            }

            String subpl = placeholder.substring(module.name().length());
            if (subpl.startsWith("_")) {
                subpl = subpl.substring(1);
            }

            return module.resolve(subpl, context);
        }

        for (PlaceholderModule<E> module : delegates) {
            Object replacement = module.resolve(placeholder, context);
            if (replacement == null) {
                continue;
            }

            return replacement;
        }

        return null;
    }

    @Override
    public <R> R resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context, @NotNull Class<R> type) throws ReplacementConversionException {
        Object replacement = resolve(placeholder, context);
        if (replacement == null) {
            return null;
        }

        if (type.isInstance(replacement)) {
            return type.cast(replacement);
        }

        if (type.isAssignableFrom(Component.class)) {
            return type.cast(Component.text(replacement.toString()));
        }
        if (type.isAssignableFrom(String.class)) {
            return type.cast(replacement.toString());
        }

        try {
            return type.cast(replacement);
        } catch (ClassCastException ignored) {
            throw new ReplacementConversionException(placeholder, replacement, type);
        }
    }

    //

    @Override
    public final void register(@NotNull PlaceholderModule<E> module) {
        if (!module.name().matches("[a-z0-9]+")) {
            throw new IllegalArgumentException("The module name may only contain lowercase letters and numbers.");
        }
        if (modules.stream().anyMatch(m -> module.name().equals(m.name())) ) {
            throw new IllegalArgumentException("A module with that name is already registered.");
        }

        modules.add(module);
        onRegister(module);
    }

    @Override
    public final void unregister(@NotNull PlaceholderModule<E> module) {
        if ( !modules.remove(module) ) {
            return;
        }

        onUnregister(module);
    }

    protected void onRegister(PlaceholderModule<E> module) {}
    protected void onUnregister(PlaceholderModule<E> module) {}

    //

    public void addDelegate(@NotNull PlaceholderModule<E> delegate) {
        delegates.add(delegate);
    }
}