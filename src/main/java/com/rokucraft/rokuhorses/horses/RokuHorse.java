package com.rokucraft.rokuhorses.horses;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.entity.ai.MobGoals;
import net.kyori.adventure.text.Component;
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
import java.util.Objects;
import java.util.UUID;

public final class RokuHorse {
    public static final double BASE_SPEED = 0.2;
    public static final double BASE_JUMP_STRENGTH = 0.6;
    private final UUID owner;
    private final Component name;
    private final Horse.Color color;
    private final Horse.Style style;

    private Horse horse;

    public RokuHorse(UUID owner, Component name, Horse.Color color, Horse.Style style) {
        this.owner = owner;
        this.name = name;
        this.color = color;
        this.style = style;
    }

    public void spawn(Location location) {
        if (horse != null) {
            horse.remove();
        }
        horse = location.getWorld().spawn(location, Horse.class, h -> {
            h.setOwner(Bukkit.getOfflinePlayer(owner));
            h.setColor(color);
            h.setStyle(style);
            h.customName(name);
            h.setCustomNameVisible(true);
            h.setPersistent(false);
            h.setRemoveWhenFarAway(true);
            h.setInvulnerable(true);
            h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            h.setJumpStrength(BASE_JUMP_STRENGTH);
            h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        });

    }

    public boolean walkTo(Player player) {
        if (horse == null) {
            throw new IllegalStateException();
        }
        Pathfinder.PathResult result = horse.getPathfinder().findPath(player);

        MobGoals goalsManager = Bukkit.getMobGoals();
        goalsManager.addGoal(horse, 0, new Goal<>() {
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
                if (horse.getLocation().distanceSquared(player.getLocation()) < 5) {
                    goalsManager.removeGoal(horse, getKey());
                    return;
                }
                horse.lookAt(player);
                horse.setTarget(player);
                horse.getPathfinder().moveTo(player, 1.5);
            }
        });

        return result != null;
    }

    public UUID owner() {
        return owner;
    }

    public Component name() {
        return name;
    }

    public Horse.Color color() {
        return color;
    }

    public Horse.Style style() {
        return style;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RokuHorse) obj;
        return Objects.equals(this.owner, that.owner) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.color, that.color) &&
                Objects.equals(this.style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, color, style);
    }

    @Override
    public String toString() {
        return "RokuHorse[" +
                "owner=" + owner + ", " +
                "name=" + name + ", " +
                "color=" + color + ", " +
                "style=" + style + ']';
    }
}