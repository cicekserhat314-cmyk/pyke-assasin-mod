package com.pyke;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pyke.ability.PykeAbilityManager;
import com.pyke.particle.GreenSpectralParticleManager;

public class PykeAssassinMod implements ModInitializer {
    public static final String MOD_ID = "pyke_assasin_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Pyke Assassin Mod!");
        
        // Register ability manager
        PykeAbilityManager.register();
        
        // Register particle manager
        GreenSpectralParticleManager.register();
        
        // Server tick event for ability cooldowns
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PykeAbilityManager.updateCooldowns(server);
        });
        
        LOGGER.info("Pyke Assassin Mod initialized successfully!");
    }
}