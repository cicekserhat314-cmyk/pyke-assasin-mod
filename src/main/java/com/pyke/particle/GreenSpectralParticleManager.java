package com.pyke.particle;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import com.pyke.ability.PykeAbilityManager;

public class GreenSpectralParticleManager {
    
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(GreenSpectralParticleManager::onServerTick);
    }
    
    private static void onServerTick(MinecraftServer server) {
        server.getWorlds().forEach(world -> {
            if (world instanceof ServerWorld serverWorld) {
                spawnGreenSpectralParticles(serverWorld);
            }
        });
    }
    
    private static void spawnGreenSpectralParticles(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers()) {
            PykeAbilityManager.PykePlayerData data = PykeAbilityManager.getPlayerData(player);
            
            // Spawn particles when invisible
            if (data.isInvisible && data.spectralParticleTimer > 0) {
                spawnInvisibilityParticles(world, player);
            }
        }
    }
    
    private static void spawnInvisibilityParticles(ServerWorld world, PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        double height = player.getHeight();
        
        // Circling green particles around the player
        long tick = world.getTime();
        double angle = (tick * 0.05) % (Math.PI * 2);
        
        for (int i = 0; i < 3; i++) {
            double currentAngle = angle + (i * Math.PI * 2 / 3);
            double radius = 0.8 + Math.sin(tick * 0.02) * 0.2;
            
            double x = playerPos.x + Math.cos(currentAngle) * radius;
            double y = playerPos.y + height / 2 + Math.sin(tick * 0.03) * 0.3;
            double z = playerPos.z + Math.sin(currentAngle) * radius;
            
            // Spawn SOUL particles (green spectral effect)
            world.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0.05);
        }
        
        // Additional trailing particles
        for (int i = 0; i < 2; i++) {
            double randomAngle = Math.random() * Math.PI * 2;
            double randomRadius = Math.random() * 1.5;
            
            double x = playerPos.x + Math.cos(randomAngle) * randomRadius;
            double y = playerPos.y + height / 2 + (Math.random() - 0.5) * height;
            double z = playerPos.z + Math.sin(randomAngle) * randomRadius;
            
            world.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0, -0.05, 0, 0.03);
        }
    }
}