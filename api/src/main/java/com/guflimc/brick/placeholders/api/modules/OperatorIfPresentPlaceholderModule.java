package com.guflimc.brick.placeholders.api.modules;

import com.guflimc.brick.placeholders.api.manager.PlaceholderManager;
import com.guflimc.brick.placeholders.api.parser.PlaceholderParser;
import com.guflimc.brick.placeholders.api.resolver.PlaceholderResolveContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class OperatorIfPresentPlaceholderModule<E> extends OperatorPlaceholderModule<E> {

    public OperatorIfPresentPlaceholderModule(PlaceholderManager<E> manager) {
        super(manager, "if_present");
    }

    @Override
    public Component resolve(@NotNull String placeholder, @NotNull PlaceholderResolveContext<E> context) {
        PlaceholderParser parser = PlaceholderParser.of(placeholder);

        String inner = parser.until('_', 1);

        Component _then = parse(parser.until('_', 1));
        Component _else = null;
        if (parser.hasNext()) {
            _else = parse(parser.until('_', 1));
        }

        Component resolved = manager.resolve(inner, context);
        if (resolved != null) {
            return replace(_then, resolved);
        }
        return _else;
    }

}
