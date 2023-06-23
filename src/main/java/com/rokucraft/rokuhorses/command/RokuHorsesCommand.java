package com.rokucraft.rokuhorses.command;

import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;

public interface RokuHorsesCommand {
    void init(CommandManager<CommandSender> manager);
}
