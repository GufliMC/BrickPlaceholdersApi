# BrickPlaceholders

A Minecraft library for handling placeholders by various plugins/extensions.

## Platforms

* [x] Minestom
* [x] Spigot / Paper

## Install

Get the latest [release](https://github.com/GufliMC/BrickPlaceholders/releases) and place it in your server.

## Usage

### Gradle

```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}

dependencies {
    // minestom
    compileOnly 'com.guflimc.brick.placeholders:minestom-api:1.0-SNAPSHOT'
    
    // spigot
    compileOnly 'com.guflimc.brick.placeholders:spigot-api:1.0-SNAPSHOT'
}
```

### Javadocs

* [Minestom](https://guflimc.github.io/BrickPlaceholders/minestom)
* [Spigot](https://guflimc.github.io/BrickPlaceholders/spigot)


### Examples

```java
SpigotPlaceholderAPI.get().registerReplacer("level", (player) -> {
    return Component.text(player.getLevel());
});

Component result = SpigotPlaceholderAPI.get().replace(player, text);
```


