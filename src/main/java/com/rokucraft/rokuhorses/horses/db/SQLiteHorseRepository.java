package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLiteHorseRepository implements HorseRepository {
    private final Jdbi jdbi;

    @Inject
    public SQLiteHorseRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }



    public Optional<RokuHorse> getByPlayerIdSync(UUID uuid) {
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

    public void updateSync(RokuHorse horse) {
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

    public void insertSync(RokuHorse horse) {
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

    @Override
    public CompletableFuture<Optional<RokuHorse>> getByPlayerId(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getByPlayerIdSync(uuid));
    }

    @Override
    public CompletableFuture<Void> update(RokuHorse horse) {
        return CompletableFuture.runAsync(() -> updateSync(horse));
    }

    @Override
    public CompletableFuture<Void> insert(RokuHorse horse) {
        return CompletableFuture.runAsync(() -> insertSync(horse));
    }
}
