package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.rokucraft.rokuhorses.HorseManager;
import com.rokucraft.rokuhorses.RokuHorses;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand {
    private final RokuHorses plugin;
    private final HorseManager horseManager;

    public SpawnCommand(RokuHorses plugin) {
        this.plugin = plugin;
        this.horseManager = plugin.getHorseManager();
    }

    public Command<CommandSender> build(Command.Builder<CommandSender> builder) {
        return builder.literal("spawn")
                .permission("rokuhorses.command.spawn")
                .senderType(Player.class)
                .handler(this::execute)
                .build();
    }

    private void execute(CommandContext<CommandSender> ctx) {
        Player sender = (Player) ctx.getSender();
        horseManager.spawnHorse(sender, sender, sender.getLocation());
    }


}
