package com.rokucraft.rokuhorses;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.entity.ai.MobGoals;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseManager {

    private final RokuHorses plugin;
    public static final double BASE_SPEED = 0.2;
    public static final double BASE_JUMP_STRENGTH = 0.6;

    private final Map<UUID, Horse> loadedHorses = new HashMap<>();

    public HorseManager(RokuHorses plugin) {
        this.plugin = plugin;
    }

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
            h.setOwner(player);
            h.setPersistent(false);
            h.setRemoveWhenFarAway(true);
            h.setInvulnerable(true);
            h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            h.setColor(Horse.Color.CREAMY);
            h.setStyle(Horse.Style.NONE);
            h.setJumpStrength(BASE_JUMP_STRENGTH);
            h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        });
        loadedHorses.put(player.getUniqueId(), horse);
    }

    public void callHorse(Audience sender, Player player) {
        Horse horse = loadedHorses.get(player.getUniqueId());
        if (horse == null || horse.getWorld() != player.getWorld()) {
            sender.sendMessage(Component.text("This horse is out of reach", NamedTextColor.RED));
            return;
        }
        if (!horse.getPassengers().isEmpty()) {
            sender.sendMessage(Component.text("This horse is currently mounted", NamedTextColor.RED));
            return;
        }
        Pathfinder.PathResult result = horse.getPathfinder().findPath(player);
        if (result == null) {
            sender.sendMessage(Component.text("This horse is out of reach", NamedTextColor.RED));
            return;
        }
        MobGoals goalsManager =  Bukkit.getMobGoals();
        goalsManager.addGoal(horse, 0, new Goal<>() {
            @Override
            public boolean shouldActivate() {
                return true;
            }

            @Override
            public @NotNull GoalKey<Horse> getKey() {
                return GoalKey.of(Horse.class, new NamespacedKey(plugin, "whistle"));
            }

            @Override
            public @NotNull EnumSet<GoalType> getTypes() {
                return EnumSet.of(GoalType.TARGET, GoalType.MOVE);
            }

            @Override
            public void tick() {
                if (horse.getLocation().distanceSquared(player.getLocation()) < 5) {
                    goalsManager.removeGoal(horse, getKey());
                    return;
                }
                horse.lookAt(player);
                horse.setTarget(player);
                horse.getPathfinder().moveTo(player, 1.5);
            }
        });
    }
}
