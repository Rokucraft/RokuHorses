package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;

import java.util.Optional;
import java.util.UUID;

public interface HorseRepository {
    Optional<RokuHorse> fetch(UUID uuid);

    void saveSync(RokuHorse horse);

    void createSync(RokuHorse horse);
}
