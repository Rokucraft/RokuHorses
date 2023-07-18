package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Horse;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class SQLiteHorseManager extends HorseManager {
    private final Jdbi jdbi;

    public SQLiteHorseManager(Path path) {
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

        this.jdbi = Jdbi.create(url);
    }

    @Override
    protected Optional<RokuHorse> fetch(UUID uuid) {
        return jdbi.withHandle(handle ->
                handle.select("SELECT owner, name, color, style FROM horse WHERE owner = :uuid")
                        .bind("uuid", uuid.toString())
                        .map((rs, ctx) -> new RokuHorse(
                                UUID.fromString(rs.getString("owner")),
                                GsonComponentSerializer.gson().deserializeOrNull(rs.getString("name")),
                                Horse.Color.valueOf(rs.getString("color")),
                                Horse.Style.valueOf(rs.getString("style"))
                        ))
                        .findOne()
        );
    }

    @Override
    protected void saveSync(RokuHorse horse) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                                UPDATE horse
                                SET
                                  name = :name,
                                  color = :color,
                                  style = :style
                                WHERE owner = :uuid
                                """)
                        .bind("name", GsonComponentSerializer.gson().serializeOrNull(horse.name()))
                        .bind("color", horse.color())
                        .bind("style", horse.style())
                        .bind("uuid", horse.owner())
                        .execute()
        );
    }

    @Override
    protected void createSync(RokuHorse horse) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                        INSERT INTO horse(owner, name, color, style)
                        VALUES (:uuid,
                                :name,
                                :color,
                                :style)
                        """)
                        .bind("uuid", horse.owner())
                        .bind("name", GsonComponentSerializer.gson().serializeOrNull(horse.name()))
                        .bind("color", horse.color())
                        .bind("style", horse.style())
                        .execute()
        );
    }
}
