package com.guflimc.brick.placeholders.common.modules;

import com.guflimc.brick.placeholders.api.PlaceholderManager;
import com.guflimc.brick.placeholders.api.module.PlaceholderModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public abstract class OperatorPlaceholderModule<E> implements PlaceholderModule<E> {

    protected final PlaceholderManager<E> manager;
    protected final String operator;

    public OperatorPlaceholderModule(PlaceholderManager<E> manager, String operator) {
        this.manager = manager;
        this.operator = operator;
    }

    @Override
    public final String name() {
        return "op_" + operator;
    }

    //

    protected Component parse(@Nullable String str) {
        if ( str == null ) {
            return Component.text("");
        }
        // TODO also support minimessage
        return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
    }

    protected Component replace(@NotNull Component container, @Nullable Object replacement) {
        if ( replacement == null ) {
            return container.replaceText(builder -> builder.match(Pattern.quote("{}"))
                    .replacement((mr, tb) -> Component.text("")));
        }

        Component r;
        if (replacement instanceof Component rc) {
            r = rc;
        } else {
            r = parse(replacement.toString());
        }

        return container.replaceText(builder -> builder.match(Pattern.quote("{}"))
                .replacement((mr, tb) -> r));
    }

}
