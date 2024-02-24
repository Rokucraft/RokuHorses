package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class SQLiteHorseRepository implements HorseRepository {
    private final Jdbi jdbi;

    public SQLiteHorseRepository(Path path) {
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
    public Optional<RokuHorse> fetch(UUID uuid) {
        return jdbi.withHandle(handle ->
                handle.select("""
                                SELECT owner,
                                       name,
                                       color,
                                       style,
                                       last_known_x,
                                       last_known_y,
                                       last_known_z,
                                       last_known_world
                                FROM horse
                                WHERE owner = :uuid
                                """)
                        .bind("uuid", uuid.toString())
                        .map((rs, ctx) -> {
                                    String lastKnownWorld = rs.getString("last_known_world");
                                    Location location = null;
                                    if (lastKnownWorld != null) {
                                        location = new Location(Bukkit.getWorld(rs.getString("last_known_world")),
                                                rs.getInt("last_known_x"),
                                                rs.getInt("last_known_y"),
                                                rs.getInt("last_known_z")
                                        );
                                    }
                                    return new RokuHorse(
                                            UUID.fromString(rs.getString("owner")),
                                            GsonComponentSerializer.gson().deserializeOrNull(rs.getString("name")),
                                            Horse.Color.valueOf(rs.getString("color")),
                                            Horse.Style.valueOf(rs.getString("style")),
                                            location
                                    );
                                }

                        )
                        .findOne()
        );
    }

    @Override
    public void saveSync(RokuHorse horse) {
        Location loc = horse.lastKnownLocation();
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                                UPDATE horse
                                SET
                                  name = :name,
                                  color = :color,
                                  style = :style,
                                  last_known_x = :last_known_x,
                                  last_known_y = :last_known_y,
                                  last_known_z = :last_known_z,
                                  last_known_world = :last_known_world
                                WHERE owner = :uuid
                                """)
                        .bind("name", GsonComponentSerializer.gson().serializeOrNull(horse.name()))
                        .bind("color", horse.color())
                        .bind("style", horse.style())
                        .bind("uuid", horse.owner())
                        .bind("last_known_x", loc != null ? loc.getBlockX() : null)
                        .bind("last_known_y", loc != null ? loc.getBlockY() : null)
                        .bind("last_known_z", loc != null ? loc.getBlockZ() : null)
                        .bind("last_known_world", loc != null ? loc.getWorld().getName() : null)
                        .execute()
        );
    }

    @Override
    public void createSync(RokuHorse horse) {
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