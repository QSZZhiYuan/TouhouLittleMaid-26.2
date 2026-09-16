package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;

public class SlashBladeCompat {
    public static boolean isSlashBladeLoaded() {
        return false;
    }

    public static boolean isSlashBladeItem(ItemStack stack) {
        return false;
    }

    public static void swingSlashBlade(EntityMaid maid, ItemStack itemInHand) {
    }
}