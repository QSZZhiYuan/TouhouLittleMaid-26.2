package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.YsmMaidInfo;
import net.minecraft.nbt.CompoundTag;

public class YsmCompat {
    public static void init() {
    }

    public static boolean isInstalled() {
        return false;
    }

    public static YsmMaidInfo getYsmMaidInfo(CompoundTag maidData) {
        return YsmMaidInfo.EMPTY;
    }
}