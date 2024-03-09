package com.rokucraft.rokuhorses;

import com.rokucraft.rokuhorses.di.DaggerRokuHorsesComponent;
import com.rokucraft.rokuhorses.di.RokuHorsesComponent;
import com.rokucraft.rokuhorses.integration.Integration;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jspecify.annotations.Nullable;

public final class RokuHorses extends JavaPlugin {

    private @Nullable RokuHorsesComponent component = null;

    @Override
    public void onLoad() {
        this.component = DaggerRokuHorsesComponent.builder()
                .plugin(this)
                .build();
        component.integrations().forEach(Integration::onLoad);
    }

    @Override
    public void onEnable() {
        if (component == null ) throw new IllegalStateException("onEnable was called before onLoad");
        PaperCommandManager<CommandSender> commandManager;
        try {
            commandManager = PaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        component.commands().forEach(cmd -> cmd.init(commandManager));
        component.integrations().forEach(Integration::initialize);
        component.listeners().forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }
}
