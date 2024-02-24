package com.rokucraft.rokuhorses.di;

import com.rokucraft.rokuhorses.command.RokuHorsesCommand;
import com.rokucraft.rokuhorses.command.commands.*;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
abstract class CommandModule {
    @Binds
    @IntoSet
    abstract RokuHorsesCommand bindDespawnCommand(DespawnCommand command);

    @Binds
    @IntoSet
    abstract RokuHorsesCommand bindEditCommand(EditCommand command);

    @Binds
    @IntoSet
    abstract RokuHorsesCommand bindNameCommand(NameCommand command);

    @Binds
    @IntoSet
    abstract RokuHorsesCommand bindSpawnCommand(SpawnCommand command);

    @Binds
    @IntoSet
    abstract RokuHorsesCommand bindWhistleCommand(WhistleCommand command);
}
