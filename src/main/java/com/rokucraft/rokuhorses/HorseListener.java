package com.rokucraft.rokuhorses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.UUID;

public class HorseListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof HorseInventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMount(EntityMountEvent e) {
        UUID ownerUUID = e.getMount().getPersistentDataContainer().get(RokuHorses.OWNER_KEY, new UUIDDataType());
        if (!e.getEntity().getUniqueId().equals(ownerUUID)) {
            e.getEntity().sendMessage(Component.text("This mount belongs to someone else", NamedTextColor.RED));
            e.setCancelled(true);
        }
    }
}
