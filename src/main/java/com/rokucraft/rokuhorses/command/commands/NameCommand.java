package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;

import javax.inject.Inject;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class NameCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    @Inject
    public NameCommand(HorseManager horseManager) {
        this.horseManager = horseManager;
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
                                    player.sendMessage(text(
                                            "Your horse's name has been changed to " + name.get(),
                                            NamedTextColor.GREEN
                                    ));
                                } else {
                                    player.sendMessage(text(
                                            "Your horse's name has been removed", NamedTextColor.GREEN
                                    ));
                                }
                            });
                        })
        );
    }
}
