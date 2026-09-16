package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ServantBellClientHelper {
    public static void openScreen(EntityMaid maid) {
        if (maid.level.isClientSide()) {
            Minecraft.getInstance().setScreenAndShow(new ServantBellSetScreen(maid));
        }
    }
}