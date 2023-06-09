package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.rokucraft.rokuhorses.HorseManager;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhistleCommand implements RokuHorsesCommand {
    private final RokuHorses plugin;
    private final HorseManager horseManager;

    public WhistleCommand(RokuHorses plugin) {
        this.plugin = plugin;
        this.horseManager = plugin.getHorseManager();
    }

    public void init(CommandManager<CommandSender> manager) {
        manager.command(manager.commandBuilder("horse").literal("whistle")
                .permission("rokuhorses.command.whistle")
                .senderType(Player.class)
                .handler(this::execute)
                .build()
        );
    }

    private void execute(CommandContext<CommandSender> ctx) {
        Player sender = (Player) ctx.getSender();
        horseManager.callHorse(sender, sender);
    }
}
