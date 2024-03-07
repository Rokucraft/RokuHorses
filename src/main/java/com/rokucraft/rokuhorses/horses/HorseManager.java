package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import org.bukkit.Bukkit;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class HorseManager {

    private final Map<UUID, CompletableFuture<@Nullable RokuHorse>> cache = new HashMap<>();
    private final HorseRepository horseRepository;

    @Inject
    public HorseManager(RokuHorses plugin, HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
        // Save horse data every 30 seconds
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> cache.values().stream()
                        .map(future -> future.getNow(null))
                        .filter(Objects::nonNull)
                        .filter(RokuHorse::isSpawned)
                        .forEach(this::save),
                20 * 30, 20 * 30);
    }

    public CompletableFuture<@Nullable RokuHorse> horse(UUID uuid) {
        return cache.computeIfAbsent(uuid, (key) -> horseRepository.getByPlayerId(uuid));
    }

    public CompletableFuture<Void> save(RokuHorse horse) {
        return horseRepository.update(horse);
    }

    public CompletableFuture<Void> create(RokuHorse horse) {
        return horseRepository.insert(horse);
    }

    public void unload(UUID uuid) {
        cache.remove(uuid);
    }
}
