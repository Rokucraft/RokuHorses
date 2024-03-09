package com.rokucraft.rokuhorses.integration.worldguard;

import com.rokucraft.rokuhorses.horses.HorseManager;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;

public class HorseEntryHandler extends FlagValueChangeHandler<StateFlag.State> {
    private final HorseManager horseManager;

    public HorseEntryHandler(Session session, HorseManager horseManager) {
        super(session, Flags.HORSE_ENTRY);
        this.horseManager = horseManager;
    }

    @Override
    protected void onInitialValue(LocalPlayer player, ApplicableRegionSet set, StateFlag.State value) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, StateFlag.State currentValue, StateFlag.State lastValue, MoveType moveType) {
        if (currentValue == StateFlag.State.ALLOW) return true;

        horseManager.horse(player.getUniqueId()).thenAccept(horse -> {
            if (horse != null) {
                horse.despawn();
            }
        });

        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, StateFlag.State lastValue, MoveType moveType) {
        return true;
    }
}
