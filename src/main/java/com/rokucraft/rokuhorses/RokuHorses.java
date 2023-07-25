package com.rokucraft.rokuhorses;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.rokucraft.rokuhorses.command.commands.*;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.horses.db.SQLiteHorseManager;
import com.rokucraft.rokuhorses.listeners.HorseListener;
import com.rokucraft.rokuhorses.listeners.PlayerJoinListener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RokuHorses extends JavaPlugin {

    private PaperCommandManager<CommandSender> commandManager;
    private HorseManager horseManager;

    @Override
    public void onEnable() {
        try {
            this.commandManager = PaperCommandManager.createNative(this, CommandExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.horseManager = new SQLiteHorseManager(getDataFolder().toPath().resolve("storage.db"));
        List.of(
                new SpawnCommand(this),
                new DespawnCommand(this),
                new WhistleCommand(this),
                new EditCommand(this),
                new NameCommand(this)
        ).forEach(cmd -> cmd.init(commandManager));
        this.getServer().getPluginManager().registerEvents(new HorseListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(horseManager), this);
    }

    public HorseManager getHorseManager() {
        return horseManager;
    }
}
