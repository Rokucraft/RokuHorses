package com.rokucraft.rokuhorses.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;

import java.util.Optional;

public class EditCommand implements RokuHorsesCommand {

    HorseManager horseManager;

    public EditCommand(RokuHorses plugin) {
        horseManager = plugin.getHorseManager();
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        var root = manager.commandBuilder("horse")
                .literal("edit")
                .argument(OfflinePlayerArgument.of("player"));
        manager.command(
                root.literal("style")
                        .permission("rokuhorses.command.edit.style")
                        .argument(EnumArgument.of(Horse.Style.class, "style"))
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Horse.Style style = ctx.get("style");
                            horseManager.horse(player.getUniqueId()).thenAccept(
                                    horse -> {
                                        horse.style(style);
                                        horseManager.save(horse);
                                    }
                            );
                        })
        );
        manager.command(
                root.literal("color")
                        .permission("rokuhorses.command.edit.color")
                        .argument(EnumArgument.of(Horse.Color.class, "color"))
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Horse.Color color = ctx.get("color");
                            horseManager.horse(player.getUniqueId()).thenAccept(
                                    horse -> {
                                        horse.color(color);
                                        horseManager.save(horse);
                                    }
                            );
                        })
        );
        manager.command(
                root.literal("name")
                        .permission("rokuhorses.command.edit.name")
                        .argument(StringArgument.optional("name", StringArgument.StringMode.GREEDY))
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Optional<String> name = ctx.getOptional("name");
                            horseManager.horse(player.getUniqueId()).thenAccept(
                                    horse -> {
                                        horse.name(name.map(Component::text).orElse(null));
                                        horseManager.save(horse);
                                    }
                            );
                        })
        );
    }
}
