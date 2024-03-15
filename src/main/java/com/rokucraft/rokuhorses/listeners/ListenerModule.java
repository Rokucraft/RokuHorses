package com.rokucraft.rokuhorses.listeners;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import org.bukkit.event.Listener;

@Module
public interface ListenerModule {
    @Binds
    @IntoSet
    Listener bindPlayerJoinListener(PlayerJoinListener listener);

    @Binds
    @IntoSet
    Listener bindHorseListener(HorseListener listener);
}
