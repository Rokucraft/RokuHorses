package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {DataModule.class})
public interface RokuHorsesComponent {
    HorseManager horseManager();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder plugin(RokuHorses plugin);
        RokuHorsesComponent build();
    }
}
