package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SWarfareCompat {
    public static boolean shouldHideLivingRender(LivingEntity entity) {
        return false;
    }

    public static boolean isVehicle(Entity entity) {
        return false;
    }
}