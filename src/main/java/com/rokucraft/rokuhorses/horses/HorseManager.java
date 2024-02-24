package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class HorseManager {

    private final Map<UUID, CompletableFuture<RokuHorse>> cache = new HashMap<>();
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

    public CompletableFuture<RokuHorse> horse(UUID uuid) {
        return cache.computeIfAbsent(uuid, key -> CompletableFuture.supplyAsync(() ->
                horseRepository.fetch(uuid).orElseGet(() -> {
                    RokuHorse newHorse = new RokuHorse(
                            uuid,
                            null,
                            Horse.Color.CHESTNUT,
                            Horse.Style.NONE
                    );
                    create(newHorse);
                    return newHorse;
                })));
    }

    public CompletableFuture<Void> save(RokuHorse horse) {
        return CompletableFuture.runAsync(() -> horseRepository.saveSync(horse));
    }

    public CompletableFuture<Void> create(RokuHorse horse) {
        return CompletableFuture.runAsync(() -> horseRepository.createSync(horse));
    }

    public CompletableFuture<Void> unload(UUID uuid) {
        cache.remove(uuid);
        return CompletableFuture.completedFuture(null);
    }
}
