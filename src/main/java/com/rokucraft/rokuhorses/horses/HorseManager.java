package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.event.HorseSpawnEvent;
import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

import static org.bukkit.entity.Horse.Color.*;

@Singleton
public class HorseManager {

    public static final Horse.Color[] DEFAULT_COLORS = new Horse.Color[]{CREAMY, BROWN, GRAY};
    private final Map<UUID, RokuHorse> data = new HashMap<>();
    private final Map<UUID, SpawnedHorse> spawned = new HashMap<>();
    private final HorseRepository horseRepository;
    private final Random random = new Random();

    @Inject
    public HorseManager(HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
    }

    public CompletableFuture<@Nullable RokuHorse> horse(UUID uuid) {
        RokuHorse cached = data.get(uuid);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
        return horseRepository.getByPlayerId(uuid).thenApply(horse -> {
            if (horse != null) data.put(uuid, horse);
            return horse;
        });
    }

    public @Nullable SpawnedHorse spawned(UUID uuid) {
        return spawned.get(uuid);
    }

    public @Nullable SpawnedHorse spawn(RokuHorse horse, Location location) {
        HorseSpawnEvent event = new HorseSpawnEvent(horse, location);
        event.callEvent();
        if (event.isCancelled()) return null;

        SpawnedHorse existing = spawned.remove(horse.owner());
        if (existing != null) existing.despawn();

        SpawnedHorse fresh = SpawnedHorse.spawn(horse, location);
        spawned.put(horse.owner(), fresh);
        return fresh;
    }

    public void despawn(UUID uuid) {
        SpawnedHorse live = spawned.remove(uuid);
        if (live != null) live.despawn();
    }

    public CompletableFuture<@Nullable RokuHorse> update(UUID uuid, UnaryOperator<RokuHorse> transform) {
        return horse(uuid).thenCompose(current -> {
            if (current == null) return CompletableFuture.completedFuture(null);
            RokuHorse updated = transform.apply(current);
            data.put(uuid, updated);
            SpawnedHorse live = spawned.get(uuid);
            if (live != null) live.applyData(updated);
            return horseRepository.update(updated).thenApply(v -> updated);
        });
    }

    public CompletableFuture<Void> create(UUID owner) {
        // TODO: Ensure player does not yet have a horse
        RokuHorse horse = new RokuHorse(
                owner,
                null,
                DEFAULT_COLORS[random.nextInt(DEFAULT_COLORS.length)],
                Horse.Style.NONE
        );
        data.put(owner, horse);
        return horseRepository.insert(horse);
    }

    public void unload(UUID uuid) {
        despawn(uuid);
        data.remove(uuid);
    }
}
