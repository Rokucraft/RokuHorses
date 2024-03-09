package com.rokucraft.rokuhorses.horses.db;

import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Horse;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLiteHorseRepository implements HorseRepository {
    private final Jdbi jdbi;

    @Inject
    public SQLiteHorseRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


    public @Nullable RokuHorse getByPlayerIdSync(UUID uuid) {
        return jdbi.withHandle(handle ->
                handle.select("""
                                SELECT owner,
                                       name,
                                       color,
                                       style,
                                FROM horse
                                WHERE owner = :uuid
                                """)
                        .bind("uuid", uuid.toString())
                        .map((rs, ctx) -> new RokuHorse(
                                        UUID.fromString(rs.getString("owner")),
                                        GsonComponentSerializer.gson().deserializeOrNull(rs.getString("name")),
                                        Horse.Color.valueOf(rs.getString("color")),
                                        Horse.Style.valueOf(rs.getString("style"))
                                )
                        ).findOne()
        ).orElse(null);
    }

    public void updateSync(RokuHorse horse) {
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
    public CompletableFuture<@Nullable RokuHorse> getByPlayerId(UUID uuid) {
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
