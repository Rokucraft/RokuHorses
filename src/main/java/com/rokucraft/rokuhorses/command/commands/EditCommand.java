package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.incendo.cloud.CommandManager;

import javax.inject.Inject;
import java.util.Optional;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.incendo.cloud.bukkit.parser.OfflinePlayerParser.offlinePlayerParser;
import static org.incendo.cloud.parser.standard.EnumParser.enumParser;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class EditCommand implements RokuHorsesCommand {

    private final HorseManager horseManager;

    @Inject
    public EditCommand(HorseManager horseManager) {
        this.horseManager = horseManager;
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        var root = manager.commandBuilder("horse")
                .literal("edit")
                .required("player", offlinePlayerParser());
        manager.command(
                root.literal("style")
                        .permission("rokuhorses.command.edit.style")
                        .required("style", enumParser(Horse.Style.class))
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Horse.Style style = ctx.get("style");
                            horseManager.update(player.getUniqueId(), h -> h.withStyle(style))
                                    .thenAccept(updated -> {
                                        if (updated == null) {
                                            ctx.sender().sendMessage("This player does not have a horse.");
                                        }
                                    });
                        })
        );
        manager.command(
                root.literal("color")
                        .permission("rokuhorses.command.edit.color")
                        .required("color", enumParser(Horse.Color.class))
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Horse.Color color = ctx.get("color");
                            horseManager.update(player.getUniqueId(), h -> h.withColor(color))
                                    .thenAccept(updated -> {
                                        if (updated == null) {
                                            ctx.sender().sendMessage("This player does not have a horse.");
                                        }
                                    });
                        })
        );
        manager.command(
                root.literal("name")
                        .permission("rokuhorses.command.edit.name")
                        .optional("name", greedyStringParser())
                        .handler(ctx -> {
                            OfflinePlayer player = ctx.get("player");
                            Optional<String> name = ctx.optional("name");
                            horseManager.update(
                                    player.getUniqueId(),
                                    h -> h.withName(name.map(miniMessage()::deserialize).orElse(null))
                            ).thenAccept(updated -> {
                                if (updated == null) {
                                    ctx.sender().sendMessage("This player does not have a horse.");
                                }
                            });
                        })
        );
    }
}
