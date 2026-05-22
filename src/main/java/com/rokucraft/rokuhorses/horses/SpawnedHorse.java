package com.rokucraft.rokuhorses.horses;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.entity.ai.MobGoals;
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

public final class SpawnedHorse {
    public static final double BASE_SPEED = 0.2;
    public static final double BASE_JUMP_STRENGTH = 0.6;

    private final Horse entity;

    private SpawnedHorse(Horse entity) {
        this.entity = entity;
    }

    static SpawnedHorse spawn(RokuHorse data, Location location) {
        Horse entity = location.getWorld().spawn(location, Horse.class, h -> {
            h.setOwner(Bukkit.getOfflinePlayer(data.owner()));
            h.setColor(data.color());
            h.setStyle(data.style());
            h.customName(data.name());
            h.setCustomNameVisible(data.name() != null);
            h.setPersistent(false);
            h.setRemoveWhenFarAway(true);
            h.setInvulnerable(true);
            h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            h.setJumpStrength(BASE_JUMP_STRENGTH);
            h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        });
        return new SpawnedHorse(entity);
    }

    void despawn() {
        entity.remove();
    }

    void applyData(RokuHorse data) {
        entity.setColor(data.color());
        entity.setStyle(data.style());
        entity.customName(data.name());
        entity.setCustomNameVisible(data.name() != null);
    }

    public boolean walkTo(Player player) {
        Pathfinder.PathResult result = entity.getPathfinder().findPath(player);

        MobGoals goalsManager = Bukkit.getMobGoals();
        goalsManager.addGoal(entity, 0, new Goal<>() {
            @Override
            public boolean shouldActivate() {
                return true;
            }

            @Override
            public @NotNull GoalKey<Horse> getKey() {
                return GoalKey.of(Horse.class, new NamespacedKey("rokuhorses", "whistle"));
            }

            @Override
            public @NotNull EnumSet<GoalType> getTypes() {
                return EnumSet.of(GoalType.TARGET, GoalType.MOVE);
            }

            @Override
            public void tick() {
                if (entity.getLocation().distanceSquared(player.getLocation()) < 5) {
                    goalsManager.removeGoal(entity, getKey());
                    return;
                }
                entity.lookAt(player);
                entity.setTarget(player);
                entity.getPathfinder().moveTo(player, 1.5);
            }
        });

        return result != null;
    }
}
