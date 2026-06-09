package com.pyke.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class DualWieldSystem {
    private static final float DUAL_WIELD_DAMAGE_BONUS = 1.50f; // 50% BONUS!!!
    private static final float DUAL_WIELD_COMBO_SPEED = 0.50f; // 50% hızlı combo
    private static final double DUAL_WIELD_HOOK_RANGE = 10.0; // +10 blocks
    private static final float DUAL_WIELD_COOLDOWN_REDUCTION = 0.70f; // 30% daha hızlı
    
    public static boolean isDualWielding(PlayerEntity player) {
        var mainHand = player.getMainHandStack();
        var offHand = player.getOffHandStack();
        
        return isSword(mainHand) && isSword(offHand);
    }
    
    private static boolean isSword(net.minecraft.item.ItemStack stack) {
        var item = stack.getItem();
        return item == Items.WOODEN_SWORD || 
               item == Items.STONE_SWORD || 
               item == Items.IRON_SWORD || 
               item == Items.GOLDEN_SWORD || 
               item == Items.DIAMOND_SWORD || 
               item == Items.NETHERITE_SWORD;
    }
    
    public static float getDamageBonus(PlayerEntity player) {
        if (isDualWielding(player)) {
            return DUAL_WIELD_DAMAGE_BONUS;
        }
        return 1.0f;
    }
    
    public static float getComboSpeedBonus(PlayerEntity player) {
        if (isDualWielding(player)) {
            return DUAL_WIELD_COMBO_SPEED;
        }
        return 1.0f;
    }
    
    public static double getHookRangeBonus(PlayerEntity player) {
        if (isDualWielding(player)) {
            return DUAL_WIELD_HOOK_RANGE;
        }
        return 0.0;
    }
    
    public static float getCooldownReductionBonus(PlayerEntity player) {
        if (isDualWielding(player)) {
            return DUAL_WIELD_COOLDOWN_REDUCTION;
        }
        return 1.0f;
    }
}
