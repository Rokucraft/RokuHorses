package com.rokucraft.rokuhorses.integration.worldguard;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Flags {

    public static final StateFlag HORSE_ENTRY = new StateFlag("horse-entry", true);
    private static final Flag<?>[] VALUES = new Flag[]{HORSE_ENTRY};

    public static Flag<?>[] values() {
        return VALUES;
    }

}
