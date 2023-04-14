package com.guflimc.brick.placeholders.common;

import com.guflimc.config.common.ConfigComment;

public class PlaceholderConfig {

    @ConfigComment("Enable/disable built-in modules here.")
    public ModulesConfig modules = new ModulesConfig();

    public static class ModulesConfig {

        public boolean player = true;
        public boolean operator = true;

    }

}
