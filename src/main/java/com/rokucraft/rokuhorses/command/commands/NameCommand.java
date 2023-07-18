package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NameCommand implements RokuHorsesCommand {
    HorseManager horseManager;

    public NameCommand(RokuHorses plugin) {
        this.horseManager = plugin.getHorseManager();
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        manager.command(
                manager.commandBuilder("horse")
                        .literal("name")
                        .permission("rokuhorses.command.name")
                        .argument(StringArgument.greedy("name"))
                        .senderType(Player.class)
                        .handler(ctx -> {
                            String name = ctx.get("name");
                            Player player = ((Player) ctx.getSender());
                            horseManager.horse(player.getUniqueId()).thenAccept(horse -> {
                                horse.name(Component.text(name));
                                horseManager.save(horse);
                                player.sendMessage(Component.text(
                                        "Your horse's name has been changed to " + name,
                                        NamedTextColor.GREEN
                                ));
                            });
                        })
        );
    }
}
