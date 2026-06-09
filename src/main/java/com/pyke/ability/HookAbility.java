package com.pyke.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;

public class HookAbility {
    private static final int HOOK_COOLDOWN = 120; // 6 seconds (20 ticks/sec)
    private static final double PULL_STRENGTH = 0.8;
    private static final double HOOK_RANGE = 25.0;
    
    public static void cast(PlayerEntity player, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }
        
        PykeAbilityManager.AbilityCooldowns cooldowns = PykeAbilityManager.getCooldowns(player);
        
        if (cooldowns.hookCooldown > 0) {
            player.sendMessage(Text.literal("§cHook cooldown: " + (cooldowns.hookCooldown / 20) + "s"), true);
            return;
        }
        
        // Check range
        double distance = player.distanceTo(target);
        if (distance > HOOK_RANGE) {
            player.sendMessage(Text.literal("§cTarget too far!"), true);
            return;
        }
        
        // Pull the target
        Vec3d direction = player.getPos().subtract(target.getPos()).normalize();
        livingTarget.addVelocity(direction.x * PULL_STRENGTH, 0.2, direction.z * PULL_STRENGTH);
        livingTarget.velocityModified = true;
        
        // Damage
        livingTarget.damage(player.getWorld().getDamageSources().playerAttack(player), 8.0f);
        
        // Effects
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d particlePos = target.getPos().add(0, target.getHeight() / 2, 0);
            
            // Green spectral particles from target to player
            for (int i = 0; i < 15; i++) {
                double offsetX = Math.cos(i * Math.PI * 2 / 15) * 0.5;
                double offsetZ = Math.sin(i * Math.PI * 2 / 15) * 0.5;
                serverWorld.spawnParticles(ParticleTypes.SOUL, 
                    particlePos.x + offsetX, particlePos.y, particlePos.z + offsetZ,
                    1, 0, 0, 0, 0.1);
            }
            
            player.playSound(SoundEvents.ENTITY_FISHING_BOBBER_THROW, 1.0f, 1.2f);
        }
        
        cooldowns.hookCooldown = HOOK_COOLDOWN;
        player.sendMessage(Text.literal("§aHook cast!"), true);
    }
}