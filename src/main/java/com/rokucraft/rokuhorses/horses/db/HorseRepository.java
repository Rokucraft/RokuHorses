package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface HorseRepository {
    CompletableFuture<Optional<RokuHorse>> getByPlayerId(UUID uuid);

    CompletableFuture<Void> update(RokuHorse horse);

    CompletableFuture<Void> insert(RokuHorse horse);
}
