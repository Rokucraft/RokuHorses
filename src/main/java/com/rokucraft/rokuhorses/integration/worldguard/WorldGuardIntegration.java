package com.rokucraft.rokuhorses.integration.worldguard;

import com.rokucraft.rokuhorses.RokuHorses;
import com.rokucraft.rokuhorses.horses.HorseManager;
import com.rokucraft.rokuhorses.integration.Integration;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.session.handler.Handler;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorldGuardIntegration implements Integration {

    private final HorseManager horseManager;
    private final Logger logger;
    private final RokuHorses plugin;

    @Inject
    public WorldGuardIntegration(
            HorseManager horseManager,
            RokuHorses plugin
    ) {
        this.horseManager = horseManager;
        this.plugin = plugin;
        this.logger = plugin.getSLF4JLogger();
    }

    public void initialize() {
        for (Flag<?> flag : Flags.values()) {
            try {
                WorldGuard.getInstance().getFlagRegistry().register(flag);
            } catch (FlagConflictException e) {
                logger.error("Unable to register flag {}", flag.getName(), e);
                logger.warn("WorldGuard integration will not be initialized.");
                return;
            }
        }
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(new Handler.Factory<>() {
            @Override
            public Handler create(Session session) {
                return new HorseEntryHandler(session, horseManager);
            }
        }, null);
        plugin.getServer().getPluginManager().registerEvents(new HorseEntryListener(), plugin);
    }
}
