package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HorseManager {

    private final Map<UUID, CompletableFuture<@Nullable RokuHorse>> cache = new HashMap<>();
    private final HorseRepository horseRepository;

    @Inject
    public HorseManager(HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
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
