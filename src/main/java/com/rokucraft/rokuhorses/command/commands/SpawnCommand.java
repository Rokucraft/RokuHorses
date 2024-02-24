package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

import javax.inject.Inject;

import static org.incendo.cloud.bukkit.parser.PlayerParser.playerParser;
import static org.incendo.cloud.bukkit.parser.location.LocationParser.locationParser;

public class SpawnCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    @Inject
    public SpawnCommand(HorseManager horseManager) {
        this.horseManager = horseManager;
    }

    public void init(CommandManager<CommandSender> manager) {
        Command.Builder<CommandSender> root = manager.commandBuilder("horse")
                .literal("spawn")
                .permission("rokuhorses.command.spawn");

        manager.command(root.senderType(Player.class).handler(ctx -> {
            Player player = ctx.sender();
            horseManager.horse(player.getUniqueId()).join().spawn(player.getLocation());
        }));

        manager.command(
                root.required("player", playerParser())
                        .required("location", locationParser())
                        .handler(ctx -> {
                            Player player = ctx.get("player");
                            Location location = ctx.get("location");
                            horseManager.horse(player.getUniqueId()).join().spawn(location);
                        })
        );
    }
}
