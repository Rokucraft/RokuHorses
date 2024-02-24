package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import dagger.BindsInstance;
import dagger.Component;
import org.bukkit.event.Listener;

import java.util.Set;

@Component(modules = {DataModule.class, ListenerModule.class})
public interface RokuHorsesComponent {
    HorseManager horseManager();

    Set<Listener> listeners();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder plugin(RokuHorses plugin);
        RokuHorsesComponent build();
    }
}
