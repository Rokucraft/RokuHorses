package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import static net.kyori.adventure.text.Component.text;

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

    private void execute(CommandContext<Player> ctx) {
        Player sender = ctx.sender();
        this.callHorse(sender, sender);
    }

    public void callHorse(Audience sender, Player player) {
        RokuHorse horse = horseManager.horse(player.getUniqueId()).join();
        if (!horse.isSpawned()) {
            sender.sendMessage(text("Your horse can't hear you!", NamedTextColor.RED));
        }
        boolean success = horse.walkTo(player);
        if (!success) {
            sender.sendMessage(text("Your horse is out of reach", NamedTextColor.RED));
        }
    }
}
