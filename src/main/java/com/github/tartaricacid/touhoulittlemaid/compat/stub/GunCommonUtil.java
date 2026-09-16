package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class GunCommonUtil {
    @Nullable
    public static Identifier getGunId(ItemStack stack) {
        return null;
    }

    public static void initAndAddTask(TaskManager manager) {
    }
}