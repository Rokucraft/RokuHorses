package com.rokucraft.rokuhorses;

import com.rokucraft.rokuhorses.command.commands.*;
import com.rokucraft.rokuhorses.di.DaggerRokuHorsesComponent;
import com.rokucraft.rokuhorses.di.RokuHorsesComponent;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.listeners.HorseListener;
import com.rokucraft.rokuhorses.listeners.PlayerJoinListener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.List;

public final class RokuHorses extends JavaPlugin {

    private PaperCommandManager<CommandSender> commandManager;
    private HorseManager horseManager;

    @Override
    public void onEnable() {
        try {
            this.commandManager = PaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RokuHorsesComponent component = DaggerRokuHorsesComponent.builder()
                .plugin(this)
                .build();
        horseManager = component.horseManager();
        List.of(
                new SpawnCommand(this),
                new DespawnCommand(this),
                new WhistleCommand(this),
                new EditCommand(this),
                new NameCommand(this)
        ).forEach(cmd -> cmd.init(commandManager));

        component.listeners().forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    public HorseManager getHorseManager() {
        return horseManager;
    }
}
