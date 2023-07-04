package com.rokucraft.rokuhorses;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.rokucraft.rokuhorses.command.commands.SpawnCommand;
import com.rokucraft.rokuhorses.command.commands.WhistleCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RokuHorses extends JavaPlugin {

    private PaperCommandManager<CommandSender> commandManager;
    private HorseManager horseManager;

    public static final NamespacedKey OWNER_KEY = new NamespacedKey("rokuhorses", "horse-owner");

    @Override
    public void onEnable() {
        try {
            this.commandManager = PaperCommandManager.createNative(this, CommandExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.horseManager = new HorseManager(this);
        List.of(
                new SpawnCommand(this),
                new WhistleCommand(this)
        ).forEach(cmd -> cmd.init(commandManager));
        this.getServer().getPluginManager().registerEvents(new HorseListener(), this);
    }

    public HorseManager getHorseManager() {
        return horseManager;
    }
}
