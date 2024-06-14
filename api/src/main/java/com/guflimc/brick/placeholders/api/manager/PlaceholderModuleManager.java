package com.guflimc.brick.placeholders.api.manager;

import com.guflimc.brick.placeholders.api.module.PlaceholderModuleResolver;
import com.guflimc.brick.placeholders.api.modules.OperatorIfPresentPlaceholderModule;
import com.guflimc.brick.placeholders.api.modules.OperatorMapPlaceholderModule;
import com.guflimc.brick.placeholders.api.modules.OperatorMapRangePlaceholderModule;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class PlaceholderModuleManager<E> extends PlaceholderModuleResolver<E> implements PlaceholderManager<E> {

    private final static Pattern PATTERN = Pattern.compile("(%[^%]+%)");

    public void registerOperators() {
        register(new OperatorIfPresentPlaceholderModule<>(this));
        register(new OperatorMapPlaceholderModule<>(this));
        register(new OperatorMapRangePlaceholderModule<>(this));
    }

    //

    @Override
    public Component replace(@NotNull String text, @NotNull PlaceholderResolveContext<E> context) {
        return replace(Component.text(text), context);
    }

    @Override
    public Component replace(@NotNull Component component, @NotNull PlaceholderResolveContext<E> context) {
        return component.replaceText(builder -> builder
                .match(PATTERN)
                .replacement((mr, tb) -> replace(mr, context))
        );
    }

    private ComponentLike replace(@NotNull MatchResult match, @NotNull PlaceholderResolveContext<E> context) {
        String group = match.group().toLowerCase();
        String placeholder = group.substring(1, group.length() - 1); // remove surrounding brackets
        Component replacement = resolve(placeholder, context);
        if (replacement != null) {
            return replacement;
        }
        return Component.text(group);
    }

}