package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.RokuHorses;
import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class HorseManager {

    private final Map<UUID, CompletableFuture<RokuHorse>> cache = new HashMap<>();

    public HorseManager(RokuHorses plugin) {
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
                fetch(uuid).orElseGet(() -> {
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
        return CompletableFuture.runAsync(() -> saveSync(horse));
    }

    public CompletableFuture<Void> create(RokuHorse horse) {
        return CompletableFuture.runAsync(() -> createSync(horse));
    }

    public CompletableFuture<Void> unload(UUID uuid) {
        cache.remove(uuid);
        return CompletableFuture.completedFuture(null);
    }

    protected abstract Optional<RokuHorse> fetch(UUID uuid);

    protected abstract void saveSync(RokuHorse horse);

    protected abstract void createSync(RokuHorse horse);
}
