package com.rokucraft.rokuhorses.integration.worldguard;

import com.rokucraft.rokuhorses.event.HorseSpawnEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.kyori.adventure.text.Component.text;

public class HorseEntryListener implements Listener {
    @EventHandler
    public void onHorseSpawn(HorseSpawnEvent e) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

        if (query.testState(BukkitAdapter.adapt(e.getLocation()), null, Flags.HORSE_ENTRY)) return;

        e.setCancelled(true);
        Player player = Bukkit.getPlayer(e.getHorse().owner());
        if (player == null) return;
        player.sendMessage(text("You can't summon your horse here!", NamedTextColor.RED));
    }
}
