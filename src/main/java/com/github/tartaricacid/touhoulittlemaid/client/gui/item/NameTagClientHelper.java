package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;

public class NameTagClientHelper {
    public static void openNameTagGui(EntityMaid maid) {
        Minecraft.getInstance().setScreenAndShow(new NameTagGui(maid));
    }
}