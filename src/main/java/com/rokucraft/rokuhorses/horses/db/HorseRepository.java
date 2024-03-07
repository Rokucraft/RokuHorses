package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface HorseRepository {
    CompletableFuture<@Nullable RokuHorse> getByPlayerId(UUID uuid);

    CompletableFuture<Void> update(RokuHorse horse);

    CompletableFuture<Void> insert(RokuHorse horse);
}
