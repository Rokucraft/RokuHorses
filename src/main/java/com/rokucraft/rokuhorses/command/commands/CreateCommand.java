package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import javax.inject.Inject;

import static net.kyori.adventure.text.Component.text;
import static org.incendo.cloud.bukkit.parser.OfflinePlayerParser.offlinePlayerParser;

public class CreateCommand implements RokuHorsesCommand {
    private final HorseManager horseManager;

    @Inject
    public CreateCommand(HorseManager horseManager) {

        this.horseManager = horseManager;
    }

    @Override
    public void init(CommandManager<CommandSender> manager) {
        manager.command(
                manager.commandBuilder("horse")
                        .literal("create")
                        .permission("rokuhorses.command.create")
                        .required("player", offlinePlayerParser())
                        .handler(this::execute)
        );
    }

    private void execute(@NonNull CommandContext<CommandSender> ctx) {
        OfflinePlayer player = ctx.get("player");
        horseManager.create(player.getUniqueId());
        ctx.sender().sendMessage(text()
                .content("A horse has been created for")
                .color(NamedTextColor.GREEN)
                .appendSpace()
                .append(text(
                        player.getName() != null
                                ? player.getName()
                                : player.getUniqueId().toString()
                ))
        );
    }
}
