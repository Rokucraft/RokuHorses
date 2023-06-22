package com.rokucraft.rokuhorses;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseManager {

    public static final double BASE_SPEED = 0.2;
    public static final double BASE_JUMP_STRENGTH = 0.6;

    private final Map<UUID, Horse> loadedHorses = new HashMap<>();
    public void spawnHorse(Audience sender, Player player, Location location) {
        Horse existingHorse = loadedHorses.get(player.getUniqueId());
        if (existingHorse != null) {
            if (!existingHorse.getPassengers().isEmpty()) {
                sender.sendMessage(Component.text("This horse is currently mounted", NamedTextColor.RED));
                return;
            }
            existingHorse.remove();
        }
        Horse horse = location.getWorld().spawn(location, Horse.class, h -> {
            h.setPersistent(false);
            h.setRemoveWhenFarAway(true);
            h.setTamed(true);
            h.setInvulnerable(true);
            h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            h.setColor(Horse.Color.CREAMY);
            h.setStyle(Horse.Style.NONE);
            h.setJumpStrength(BASE_JUMP_STRENGTH);
            h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        });
        loadedHorses.put(player.getUniqueId(), horse);
    }
}
