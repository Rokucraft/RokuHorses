package com.rokucraft.rokuhorses.listeners;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;
import java.util.Optional;

public class PlayerJoinListener implements Listener {
    private final RokuHorses plugin;
    private final HorseManager manager;

    @Inject
    public PlayerJoinListener(RokuHorses plugin, HorseManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        manager.horse(e.getPlayer().getUniqueId()).thenAccept(optionalHorse -> {
            if (optionalHorse.isEmpty()) return;
            RokuHorse horse = optionalHorse.get();
            Location location = horse.lastKnownLocation();
            if (location != null) {
                Bukkit.getScheduler().runTask(plugin, () -> horse.spawn(location));
            }
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Optional<RokuHorse> optionalHorse = manager.horse(e.getPlayer().getUniqueId()).getNow(Optional.empty());
        if (optionalHorse.isPresent()) {
            RokuHorse horse = optionalHorse.get();
            manager.save(horse).thenRun(() -> Bukkit.getScheduler().runTask(plugin, horse::despawn));
        }
        manager.unload(e.getPlayer().getUniqueId());
    }

}
