package com.rokucraft.rokuhorses.integration;

import com.rokucraft.rokuhorses.integration.worldguard.WorldGuardIntegration;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface IntegrationsModule {
    @Binds
    @IntoSet
    Integration bindWorldGuardIntegration(WorldGuardIntegration integration);
}
