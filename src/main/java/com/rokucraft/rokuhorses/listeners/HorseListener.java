package com.rokucraft.rokuhorses.listeners;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;
import org.spigotmc.event.entity.EntityMountEvent;

import javax.inject.Inject;

import static net.kyori.adventure.text.Component.text;

public class HorseListener implements Listener {

    @Inject
    public HorseListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof HorseInventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMount(EntityMountEvent e) {
        if (e.getMount() instanceof Tameable mount && !e.getEntity().getUniqueId().equals(mount.getOwnerUniqueId())) {
            e.getEntity().sendMessage(text("This mount belongs to someone else", NamedTextColor.RED));
            e.setCancelled(true);
        }
    }
}
