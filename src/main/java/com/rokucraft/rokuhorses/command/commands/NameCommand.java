package com.rokucraft.rokuhorses.command.commands;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.horses.HorseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.Nullable;

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
                            renameHorse(player, name.map(Component::text).orElse(null));
                        }));
    }

    private void renameHorse(Player player, @Nullable Component name) {
        horseManager.horse(player.getUniqueId())
                .thenAccept(horse -> {
                    if (horse == null) {
                        player.sendMessage(text("You do not have a horse.", NamedTextColor.RED));
                        return;
                    }
                    horse.name(name);
                    horseManager.save(horse);
                    if (name != null) {
                        player.sendMessage(text("Your horse's name has been changed to " + name, NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(text("Your horse's name has been removed", NamedTextColor.GREEN));
                    }
                });
    }
}
