package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;

import java.util.Optional;

import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

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
                        .optional("name", greedyStringParser())
                        .senderType(Player.class)
                        .handler(ctx -> {
                            Optional<String> name = ctx.optional("name");
                            Player player = ctx.sender();
                            horseManager.horse(player.getUniqueId()).thenAccept(horse -> {
                                horse.name(name.map(Component::text).orElse(null));
                                horseManager.save(horse);
                                if (name.isPresent()) {
                                    player.sendMessage(Component.text(
                                            "Your horse's name has been changed to " + name.get(),
                                            NamedTextColor.GREEN
                                    ));
                                } else {
                                    player.sendMessage(Component.text(
                                            "Your horse's name has been removed", NamedTextColor.GREEN
                                    ));
                                }
                            });
                        })
        );
    }
}
