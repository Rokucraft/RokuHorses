package com.rokucraft.rokuhorses.integration;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.integration.worldguard.WorldGuardIntegration;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import org.bukkit.Bukkit;

import java.util.Set;

@Module
public abstract class IntegrationsModule {
    @Provides
    @ElementsIntoSet
    static Set<Integration> provideWorldGuardIntegration(HorseManager manager, RokuHorses plugin) {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            return Set.of();
        }
        return Set.of(new WorldGuardIntegration(manager, plugin));
    }
}
