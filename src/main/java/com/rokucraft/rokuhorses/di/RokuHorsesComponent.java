package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.integration.Integration;
import com.rokucraft.rokuhorses.integration.IntegrationsModule;
import dagger.BindsInstance;
import dagger.Component;
import org.bukkit.event.Listener;

import javax.inject.Singleton;
import java.util.Set;

@Component(modules = {DataModule.class, ListenerModule.class, CommandModule.class, IntegrationsModule.class})
@Singleton
public interface RokuHorsesComponent {

    Set<Listener> listeners();

    Set<RokuHorsesCommand> commands();

    Set<Integration> integrations();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder plugin(RokuHorses plugin);
        RokuHorsesComponent build();
    }
}
