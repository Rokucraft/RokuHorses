package com.rokucraft.rokuhorses.horses;

import org.bukkit.entity.Horse;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class HorseManager implements Listener {

    private final Map<UUID, CompletableFuture<RokuHorse>> cache = new HashMap<>();


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
