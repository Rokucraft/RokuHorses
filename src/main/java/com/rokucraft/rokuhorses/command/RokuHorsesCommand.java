package com.rokucraft.rokuhorses.command;

import cloud.commandframework.Command;
import org.bukkit.command.CommandSender;

public interface RokuHorsesCommand {
    Command<CommandSender> build(Command.Builder<CommandSender> builder);
}
