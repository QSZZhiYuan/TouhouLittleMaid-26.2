package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ItemChairShow extends Item {
    public ItemChairShow(Item.Properties properties) {
        super(properties.stacksTo(1));
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Item.TooltipContext pLevel, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.accept((Component.translatable("tooltips.touhou_little_maid.chair_show.desc")).withStyle(ChatFormatting.GRAY));
    }
}
