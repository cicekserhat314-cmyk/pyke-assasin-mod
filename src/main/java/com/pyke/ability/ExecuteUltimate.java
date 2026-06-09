package com.pyke.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ExecuteUltimate {
    private static final int EXECUTE_COOLDOWN = 200; // 10 seconds
    private static final float EXECUTE_DAMAGE = 25.0f;
    private static final float EXECUTE_THRESHOLD = 0.25f; // Execute if target HP < 25% max HP
    private static final double EXECUTE_RANGE = 20.0;
    
    public static void tryExecute(PlayerEntity player, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }
        
        PykeAbilityManager.AbilityCooldowns cooldowns = PykeAbilityManager.getCooldowns(player);
        
        // Check if ability is available
        if (cooldowns.executeUltimateCooldown > 0) {
            return;
        }
        
        // Check range
        double distance = player.distanceTo(target);
        if (distance > EXECUTE_RANGE) {
            return;
        }
        
        // Check if target is low health (execute condition)
        float targetMaxHealth = livingTarget.getMaxHealth();
        float targetCurrentHealth = livingTarget.getHealth();
        float healthThreshold = targetMaxHealth * EXECUTE_THRESHOLD;
        
        if (targetCurrentHealth > healthThreshold) {
            return; // Target is not low enough to execute
        }
        
        // Execute the target
        executeTarget(player, livingTarget);
        cooldowns.executeUltimateCooldown = EXECUTE_COOLDOWN;
    }
    
    private static void executeTarget(PlayerEntity player, LivingEntity target) {
        // Deal massive damage
        target.damage(player.getWorld().getDamageSources().playerAttack(player), EXECUTE_DAMAGE);
        
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);
            
            // Green spectral explosion particles
            for (int i = 0; i < 40; i++) {
                double angle = Math.random() * Math.PI * 2;
                double speed = Math.random() * 0.5 + 0.3;
                double vx = Math.cos(angle) * speed;
                double vy = Math.random() * 0.5 + 0.3;
                double vz = Math.sin(angle) * speed;
                
                serverWorld.spawnParticles(ParticleTypes.SOUL, 
                    targetPos.x, targetPos.y, targetPos.z,
                    1, vx, vy, vz, 0.15);
            }
            
            // Sound effect
            player.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.5f);
            
            // Knockback
            target.addVelocity(0, 0.8, 0);
            target.velocityModified = true;
        }
        
        player.sendMessage(net.minecraft.text.Text.literal("§6⚔ EXECUTE!"), true);
    }
}