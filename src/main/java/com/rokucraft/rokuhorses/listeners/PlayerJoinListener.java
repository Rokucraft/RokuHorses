package com.rokucraft.rokuhorses.listeners;

import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    private final HorseManager manager;

    public PlayerJoinListener(HorseManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        manager.horse(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        RokuHorse horse = manager.horse(e.getPlayer().getUniqueId()).getNow(null);
        if (horse != null) {
            horse.despawn();
        }
        manager.unload(e.getPlayer().getUniqueId());
    }

}
