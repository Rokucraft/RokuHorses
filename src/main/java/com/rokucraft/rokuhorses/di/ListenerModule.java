package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.listeners.HorseListener;
import com.rokucraft.rokuhorses.listeners.PlayerJoinListener;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import org.bukkit.event.Listener;

@Module
abstract class ListenerModule {
    @Binds
    @IntoSet
    abstract Listener bindPlayerJoinListener(PlayerJoinListener listener);

    @Binds
    @IntoSet
    abstract Listener bindHorseListener(HorseListener listener);
}
