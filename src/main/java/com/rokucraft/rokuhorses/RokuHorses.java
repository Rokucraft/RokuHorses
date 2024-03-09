package com.rokucraft.rokuhorses;

import com.rokucraft.rokuhorses.di.DaggerRokuHorsesComponent;
import com.rokucraft.rokuhorses.di.RokuHorsesComponent;
import com.rokucraft.rokuhorses.integration.Integration;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

public final class RokuHorses extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager<CommandSender> commandManager;
        try {
            commandManager = PaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RokuHorsesComponent component = DaggerRokuHorsesComponent.builder()
                .plugin(this)
                .build();

        component.commands().forEach(cmd -> cmd.init(commandManager));
        component.listeners().forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
        component.integrations().forEach(Integration::initialize);
    }
}
