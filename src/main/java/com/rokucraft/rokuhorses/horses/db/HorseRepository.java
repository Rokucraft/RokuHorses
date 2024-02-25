package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;

import java.util.Optional;
import java.util.UUID;

public interface HorseRepository {
    Optional<RokuHorse> getByPlayerId(UUID uuid);

    void update(RokuHorse horse);

    void insert(RokuHorse horse);
}
