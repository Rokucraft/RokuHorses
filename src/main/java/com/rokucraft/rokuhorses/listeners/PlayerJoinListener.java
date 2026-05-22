package com.rokucraft.rokuhorses.listeners;

import com.rokucraft.rokuhorses.horses.HorseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

public class PlayerJoinListener implements Listener {
    private final HorseManager manager;

    @Inject
    public PlayerJoinListener(HorseManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        manager.unload(e.getPlayer().getUniqueId());
    }

}
