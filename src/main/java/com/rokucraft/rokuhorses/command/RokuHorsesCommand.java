package com.rokucraft.rokuhorses.command;

import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;

public interface RokuHorsesCommand {
    void init(CommandManager<CommandSender> manager);
}
