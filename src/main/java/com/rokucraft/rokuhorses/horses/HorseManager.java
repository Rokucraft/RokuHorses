package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import org.bukkit.entity.Horse;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.bukkit.entity.Horse.Color.*;

public class HorseManager {

    public static final Horse.Color[] DEFAULT_COLORS = new Horse.Color[]{CREAMY, BROWN, GRAY};
    private final Map<UUID, CompletableFuture<@Nullable RokuHorse>> cache = new HashMap<>();
    private final HorseRepository horseRepository;
    private final Random random = new Random();

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

    public CompletableFuture<Void> create(UUID owner) {
        // TODO: Ensure player does not yet have a horse
        return horseRepository.insert(
                new RokuHorse(
                        owner,
                        null,
                        DEFAULT_COLORS[random.nextInt(DEFAULT_COLORS.length)],
                        Horse.Style.NONE
                )
        );
    }

    public void unload(UUID uuid) {
        cache.remove(uuid);
    }
}
