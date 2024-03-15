package com.rokucraft.rokuhorses.command;

import com.rokucraft.rokuhorses.command.commands.*;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface CommandModule {
    @Binds
    @IntoSet
    RokuHorsesCommand bindDespawnCommand(DespawnCommand command);

    @Binds
    @IntoSet
    RokuHorsesCommand bindEditCommand(EditCommand command);

    @Binds
    @IntoSet
    RokuHorsesCommand bindNameCommand(NameCommand command);

    @Binds
    @IntoSet
    RokuHorsesCommand bindSpawnCommand(SpawnCommand command);

    @Binds
    @IntoSet
    RokuHorsesCommand bindWhistleCommand(WhistleCommand command);

    @Binds
    @IntoSet
    CreateCommand bindCreateCommand(CreateCommand command);
}
