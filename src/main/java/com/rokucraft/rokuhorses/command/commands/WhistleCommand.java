package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import javax.inject.Inject;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class WhistleCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    @Inject
    public WhistleCommand(HorseManager horseManager) {
        this.horseManager = horseManager;
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
        Optional<RokuHorse> optionalHorse = horseManager.horse(player.getUniqueId()).join();
        if (optionalHorse.isEmpty()) {
            sender.sendMessage(text(sender == player ? "You do not have a horse!" : "This player does not have a horse!"));
            return;
        }
        RokuHorse horse = optionalHorse.get();
        if (!horse.isSpawned()) {
            sender.sendMessage(text("Your horse can't hear you!", NamedTextColor.RED));
            return;
        }
        boolean success = horse.walkTo(player);
        if (!success) {
            sender.sendMessage(text("Your horse is out of reach", NamedTextColor.RED));
        }
    }
}
