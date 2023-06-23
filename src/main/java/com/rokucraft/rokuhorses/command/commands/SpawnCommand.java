package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.bukkit.parsers.location.LocationArgument;
import com.rokucraft.rokuhorses.HorseManager;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements RokuHorsesCommand {
    private final RokuHorses plugin;
    private final HorseManager horseManager;

    public SpawnCommand(RokuHorses plugin) {
        this.plugin = plugin;
        this.horseManager = plugin.getHorseManager();
    }

    public void init(CommandManager<CommandSender> manager) {
        Command.Builder<CommandSender> root = manager.commandBuilder("horse")
                .literal("spawn")
                .permission("rokuhorses.command.spawn");

        manager.command(root.senderType(Player.class).handler(ctx -> {
            Player player = (Player) ctx.getSender();
            horseManager.spawnHorse(player, player, player.getLocation());
        }));

        manager.command(root.argument(PlayerArgument.of("player"))
                .argument(LocationArgument.of("location"))
                .handler(ctx -> {
                    Player player = ctx.get("player");
                    Location location = ctx.get("location");
                    horseManager.spawnHorse(ctx.getSender(), player, location);
                })
        );
    }
}
