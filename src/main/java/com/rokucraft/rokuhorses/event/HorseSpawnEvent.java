package com.rokucraft.rokuhorses.event;

import com.rokucraft.rokuhorses.horses.RokuHorse;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HorseSpawnEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final RokuHorse horse;
    private final Location location;

    public HorseSpawnEvent(RokuHorse horse, Location location) {
        this.horse = horse;
        this.location = location;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public RokuHorse getHorse() {
        return horse;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Location getLocation() {
        return location;
    }
}
