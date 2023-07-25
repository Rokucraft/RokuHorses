package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DespawnCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    public DespawnCommand(RokuHorses plugin) {
        this.horseManager = plugin.getHorseManager();
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        Command.Builder<CommandSender> root = manager.commandBuilder("horse")
                .literal("despawn");

        manager.command(root.senderType(Player.class)
                .permission("rokuhorses.command.despawn.self")
                .handler(ctx -> {
                    Player player = (Player) ctx.getSender();
                    RokuHorse horse = horseManager.horse(player.getUniqueId()).getNow(null);
                    if (horse != null) {
                        horse.despawn();
                    }
                }));

        manager.command(root.argument(PlayerArgument.of("player"))
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
