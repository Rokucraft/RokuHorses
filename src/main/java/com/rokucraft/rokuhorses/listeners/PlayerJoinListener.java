package com.rokucraft.rokuhorses.listeners;

import com.rokucraft.rokuhorses.horses.HorseManager;
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
        manager.unload(e.getPlayer().getUniqueId());
    }

}
