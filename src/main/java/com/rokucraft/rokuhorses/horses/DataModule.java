package com.rokucraft.rokuhorses.horses;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.db.HorseRepository;
import com.rokucraft.rokuhorses.horses.db.SQLiteHorseRepository;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Module
public abstract class DataModule {
    @Binds
    abstract HorseRepository provideHorseRepository(SQLiteHorseRepository repository);

    @Provides
    @Singleton
    static Jdbi provideJdbi(RokuHorses plugin) {
        Path path = plugin.getDataFolder().toPath().resolve("storage.db");
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create parent directories for " + path);
        }
        String url = "jdbc:sqlite:" + path;
        Flyway.configure(RokuHorses.class.getClassLoader())
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .dataSource(url, null, null)
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .load()
                .migrate();

        return Jdbi.create(url);
    }
}
