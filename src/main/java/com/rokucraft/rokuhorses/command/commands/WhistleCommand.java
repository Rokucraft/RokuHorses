package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.RokuHorse;
import com.rokucraft.rokuhorses.horses.SpawnedHorse;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import javax.inject.Inject;
import java.util.UUID;

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
        UUID uuid = player.getUniqueId();
        RokuHorse horse = horseManager.horse(uuid).join();
        if (horse == null) {
            sender.sendMessage(text("You do not have a horse!", NamedTextColor.RED));
            return;
        }
        SpawnedHorse spawned = horseManager.spawned(uuid);
        if (spawned == null) {
            horseManager.spawn(horse, player.getLocation());
            return;
        }
        if (!spawned.walkTo(player)) {
            horseManager.spawn(horse, player.getLocation());
        }
    }
}
