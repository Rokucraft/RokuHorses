package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

import static org.incendo.cloud.bukkit.parser.PlayerParser.playerParser;

public class DespawnCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    public DespawnCommand(RokuHorses plugin) {
        this.horseManager = plugin.getHorseManager();
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        Command.Builder<CommandSender> root = manager.commandBuilder("horse")
                .literal("despawn");

        manager.command(
                root.senderType(Player.class)
                        .permission("rokuhorses.command.despawn.self")
                        .handler(ctx -> {
                            Player player = ctx.sender();
                            RokuHorse horse = horseManager.horse(player.getUniqueId()).getNow(null);
                            if (horse != null) {
                                horse.despawn();
                            }
                        })
        );

        manager.command(
                root
                        .required("player", playerParser())
                        .permission("rokuhorses.command.despawn.others")
                        .handler(ctx -> {
                            Player player = ctx.get("player");
                            RokuHorse horse = horseManager.horse(player.getUniqueId()).getNow(null);
                            if (horse != null) {
                                horse.despawn();
                            }
                        })
        );
    }
}
