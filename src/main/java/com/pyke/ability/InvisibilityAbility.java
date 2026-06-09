package com.pyke.ability;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class InvisibilityAbility {
    private static final int INVISIBILITY_COOLDOWN = 300; // 15 seconds
    private static final int INVISIBILITY_DURATION = 200; // 10 seconds
    private static final int INVISIBILITY_AMPLIFIER = 0; // Invisibility I
    
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            // Cast invisibility when holding specific item and sneaking
            if (!world.isClient && player != null && player.isSneaking()) {
                if (player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                    castInvisibility(player);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
    
    public static void castInvisibility(PlayerEntity player) {
        PykeAbilityManager.AbilityCooldowns cooldowns = PykeAbilityManager.getCooldowns(player);
        
        if (cooldowns.invisibilityCooldown > 0) {
            player.sendMessage(net.minecraft.text.Text.literal("§cInvisibility cooldown: " + (cooldowns.invisibilityCooldown / 20) + "s"), true);
            return;
        }
        
        // Apply invisibility effect
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, INVISIBILITY_DURATION, INVISIBILITY_AMPLIFIER, false, false));
        
        // Apply speed effect for stealth
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, INVISIBILITY_DURATION, 1, false, false));
        
        PykeAbilityManager.PykePlayerData data = PykeAbilityManager.getPlayerData(player);
        data.isInvisible = true;
        data.spectralParticleTimer = INVISIBILITY_DURATION;
        
        // Effects
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d playerPos = player.getPos().add(0, player.getHeight() / 2, 0);
            
            // Green spectral particles around player
            for (int i = 0; i < 30; i++) {
                double angle = Math.random() * Math.PI * 2;
                double radius = 1.0;
                double x = playerPos.x + Math.cos(angle) * radius;
                double y = playerPos.y + (Math.random() - 0.5) * 2;
                double z = playerPos.z + Math.sin(angle) * radius;
                
                serverWorld.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0.1);
            }
            
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
        
        cooldowns.invisibilityCooldown = INVISIBILITY_COOLDOWN;
        cooldowns.invisibilityDuration = INVISIBILITY_DURATION;
        
        player.sendMessage(net.minecraft.text.Text.literal("§bInvisibility activated! Duration: 10s"), true);
    }
}