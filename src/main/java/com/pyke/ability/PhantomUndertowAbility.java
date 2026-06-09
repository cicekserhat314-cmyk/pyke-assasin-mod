package com.pyke.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;

public class PhantomUndertowAbility {
    private static final int E_ABILITY_COOLDOWN = 120; // 6 seconds
    private static final double DASH_DISTANCE = 15.0;
    private static final float PHANTOM_DAMAGE = 20.0f;
    private static final double PHANTOM_RANGE = 8.0;
    private static final double PHANTOM_SPEED = 2.0; // Hızlı phantom
    
    // Phantom entity tracking
    private static class PhantomProjectile {
        Vec3d position;
        Vec3d direction;
        double speed;
        int lifetime;
        PlayerEntity owner;
        boolean hasHit = false;
        
        PhantomProjectile(Vec3d pos, Vec3d dir, PlayerEntity owner) {
            this.position = pos;
            this.direction = dir.normalize();
            this.speed = PHANTOM_SPEED;
            this.lifetime = 100; // 5 seconds
            this.owner = owner;
        }
    }
    
    private static final java.util.Map<java.util.UUID, PhantomProjectile> activePhantoms = new java.util.HashMap<>();
    
    public static void cast(PlayerEntity player) {
        PykeAbilityManager.AbilityCooldowns cooldowns = PykeAbilityManager.getCooldowns(player);
        
        if (cooldowns.eAbilityCooldown > 0) {
            return;
        }
        
        // Dash forward
        performDash(player);
        
        // Spawn 1 phantom projectile
        spawnPhantomProjectile(player);
        
        cooldowns.eAbilityCooldown = E_ABILITY_COOLDOWN;
        player.sendMessage(Text.literal("§b⚫ PHANTOM UNDERTOW!"), true);
    }
    
    private static void performDash(PlayerEntity player) {
        Vec3d lookDirection = player.getRotationVector().normalize();
        Vec3d dashVelocity = lookDirection.multiply(DASH_DISTANCE * 0.05);
        
        player.addVelocity(dashVelocity.x, 0.2, dashVelocity.z);
        player.velocityModified = true;
        
        // Dash particle trail
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 15; i++) {
                Vec3d particlePos = player.getPos().add(
                    (Math.random() - 0.5) * 1, 
                    Math.random() * 2, 
                    (Math.random() - 0.5) * 1
                );
                serverWorld.spawnParticles(ParticleTypes.SOUL,
                    particlePos.x, particlePos.y, particlePos.z,
                    1, 0.1, 0.1, 0.1, 0.15);
            }
        }
    }
    
    private static void spawnPhantomProjectile(PlayerEntity player) {
        Vec3d playerPos = player.getPos().add(0, player.getHeight() / 2, 0);
        Vec3d lookDirection = player.getRotationVector().normalize();
        
        // Phantom arkadan geliyor
        Vec3d phantomStartPos = playerPos.subtract(lookDirection.multiply(8));
        
        PhantomProjectile phantom = new PhantomProjectile(phantomStartPos, lookDirection, player);
        activePhantoms.put(java.util.UUID.randomUUID(), phantom);
    }
    
    public static void updatePhantoms(ServerWorld world) {
        var iterator = activePhantoms.entrySet().iterator();
        
        while (iterator.hasNext()) {
            var entry = iterator.next();
            PhantomProjectile phantom = entry.getValue();
            
            phantom.lifetime--;
            phantom.position = phantom.position.add(phantom.direction.multiply(phantom.speed * 0.1));
            
            // Phantom çizileri
            world.spawnParticles(ParticleTypes.SOUL,
                phantom.position.x, phantom.position.y, phantom.position.z,
                3, 0, 0, 0, 0.2);
            
            // Collision check - düşman vuruş?
            if (!phantom.hasHit) {
                for (Entity entity : world.getOtherEntities(phantom.owner,
                    phantom.position.getSquaredDistance(0, 0, 0) < PHANTOM_RANGE * PHANTOM_RANGE
                        ? phantom.position.add(PHANTOM_RANGE, PHANTOM_RANGE, PHANTOM_RANGE)
                          .toBox()
                        : phantom.position.toBox())) {
                    
                    if (entity instanceof LivingEntity living && entity != phantom.owner) {
                        // Hit! Hasar ve stun
                        living.damage(world.getDamageSources().playerAttack(phantom.owner), PHANTOM_DAMAGE);
                        
                        // Stun - hareketi durdur
                        living.setVelocity(0, 0, 0);
                        living.velocityModified = true;
                        
                        // Knockback
                        Vec3d knockDir = living.getPos().subtract(phantom.position).normalize();
                        living.addVelocity(knockDir.x * 0.5, 0.3, knockDir.z * 0.5);
                        living.velocityModified = true;
                        
                        phantom.hasHit = true;
                        
                        // Hit particle explosion
                        for (int i = 0; i < 30; i++) {
                            double angle = Math.random() * Math.PI * 2;
                            double vx = Math.cos(angle) * 0.5;
                            double vy = Math.random() * 0.5;
                            double vz = Math.sin(angle) * 0.5;
                            
                            world.spawnParticles(ParticleTypes.SOUL,
                                phantom.position.x, phantom.position.y, phantom.position.z,
                                2, vx, vy, vz, 0.3);
                        }
                        
                        phantom.owner.playSound(SoundEvents.ENTITY_PHANTOM_BITE, 1.0f, 1.0f);
                        break;
                    }
                }
            }
            
            // Lifetime check
            if (phantom.lifetime <= 0 || phantom.hasHit) {
                iterator.remove();
            }
        }
    }
}
