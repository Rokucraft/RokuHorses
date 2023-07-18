package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhistleCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    public WhistleCommand(RokuHorses plugin) {
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
        this.callHorse(sender, sender);
    }

    public void callHorse(Audience sender, Player player) {
        RokuHorse horse = horseManager.horse(player.getUniqueId()).join();
        boolean success = horse.walkTo(player);
        if (!success) {
            sender.sendMessage(Component.text("This horse is out of reach", NamedTextColor.RED));
        }
    }
}
