package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import com.rokucraft.rokuhorses.horses.db.SQLiteHorseRepository;
import dagger.Module;
import dagger.Provides;

@Module
class DataModule {
    @Provides
    static HorseRepository provideHorseRepository(RokuHorses plugin) {
        return new SQLiteHorseRepository(plugin.getDataFolder().toPath().resolve("storage.db"));
    };
}
