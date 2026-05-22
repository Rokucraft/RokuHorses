package com.rokucraft.rokuhorses.horses;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Horse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record RokuHorse(
        UUID owner,
        @Nullable Component name,
        Horse.Color color,
        Horse.Style style
) {
    public RokuHorse withName(@Nullable Component name) {
        return new RokuHorse(owner, name, color, style);
    }

    public RokuHorse withColor(Horse.Color color) {
        return new RokuHorse(owner, name, color, style);
    }

    public RokuHorse withStyle(Horse.Style style) {
        return new RokuHorse(owner, name, color, style);
    }
}
