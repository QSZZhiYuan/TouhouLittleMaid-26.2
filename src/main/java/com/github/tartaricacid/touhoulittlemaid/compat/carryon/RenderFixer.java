package com.github.tartaricacid.touhoulittlemaid.compat.carryon;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

public class RenderFixer {
    private static final String CARRY_ON_ID = "carryon";

    /**
     * 修复 Carry On 模组渲染附魔物品时的 BUG
     * 当 fixedBuffers 为 5 时，就是 Carry On 模组的渲染器，此时禁止渲染手部物品
     */
    public static boolean isCarryOnRender(ItemStack stack, SubmitNodeCollector bufferSource) {

        return false;
    }
}
