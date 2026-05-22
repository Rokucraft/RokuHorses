package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

import javax.inject.Inject;

import static org.incendo.cloud.bukkit.parser.PlayerParser.playerParser;

public class DespawnCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    @Inject
    public DespawnCommand(HorseManager horseManager) {
        this.horseManager = horseManager;
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        Command.Builder<CommandSender> root = manager.commandBuilder("horse")
                .literal("despawn");

        manager.command(
                root.senderType(Player.class)
                        .permission("rokuhorses.command.despawn.self")
                        .handler(ctx -> horseManager.despawn(ctx.sender().getUniqueId()))
        );

        manager.command(
                root
                        .required("player", playerParser())
                        .permission("rokuhorses.command.despawn.others")
                        .handler(ctx -> {
                            Player player = ctx.get("player");
                            horseManager.despawn(player.getUniqueId());
                        })
        );
    }
}
