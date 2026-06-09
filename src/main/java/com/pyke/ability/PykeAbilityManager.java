package com.pyke.ability;

import java.util.*;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class PykeAbilityManager {
    private static final Map<UUID, AbilityCooldowns> playerCooldowns = new HashMap<>();
    private static final Map<UUID, PykePlayerData> playerData = new HashMap<>();
    
    public static class AbilityCooldowns {
        public int hookCooldown = 0;
        public int executeUltimateCooldown = 0;
        public int invisibilityCooldown = 0;
        public int invisibilityDuration = 0;
    }
    
    public static class PykePlayerData {
        public boolean isInvisible = false;
        public int spectralParticleTimer = 0;
    }
    
    public static void register() {
        // Hook ability - Right click on entity with SHIFT
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && player != null && player.isSneaking()) {
                if (player.getMainHandStack().isEmpty()) {
                    HookAbility.cast(player, entity);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
        
        // Attack entity for Execute Ultimate
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && player != null && player.getHealth() > 0) {
                ExecuteUltimate.tryExecute(player, entity);
            }
            return ActionResult.PASS;
        });
    }
    
    public static AbilityCooldowns getCooldowns(PlayerEntity player) {
        return playerCooldowns.computeIfAbsent(player.getUuid(), k -> new AbilityCooldowns());
    }
    
    public static PykePlayerData getPlayerData(PlayerEntity player) {
        return playerData.computeIfAbsent(player.getUuid(), k -> new PykePlayerData());
    }
    
    public static void updateCooldowns(MinecraftServer server) {
        playerCooldowns.forEach((uuid, cooldowns) -> {
            if (cooldowns.hookCooldown > 0) cooldowns.hookCooldown--;
            if (cooldowns.executeUltimateCooldown > 0) cooldowns.executeUltimateCooldown--;
            if (cooldowns.invisibilityCooldown > 0) cooldowns.invisibilityCooldown--;
            if (cooldowns.invisibilityDuration > 0) cooldowns.invisibilityDuration--;
        });
        
        playerData.forEach((uuid, data -> {
            if (data.spectralParticleTimer > 0) data.spectralParticleTimer--;
        }));
    }
    
    public static boolean isHookAvailable(PlayerEntity player) {
        return getCooldowns(player).hookCooldown <= 0;
    }
    
    public static boolean isExecuteUltimateAvailable(PlayerEntity player) {
        return getCooldowns(player).executeUltimateCooldown <= 0;
    }
    
    public static boolean isInvisibilityAvailable(PlayerEntity player) {
        return getCooldowns(player).invisibilityCooldown <= 0;
    }
}