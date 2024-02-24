package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import dagger.BindsInstance;
import dagger.Component;
import org.bukkit.event.Listener;

import java.util.Set;

@Component(modules = {DataModule.class, ListenerModule.class, CommandModule.class})
public interface RokuHorsesComponent {

    Set<Listener> listeners();

    Set<RokuHorsesCommand> commands();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder plugin(RokuHorses plugin);
        RokuHorsesComponent build();
    }
}
