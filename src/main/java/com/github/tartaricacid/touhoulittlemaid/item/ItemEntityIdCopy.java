package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ItemEntityIdCopy extends Item {
    public ItemEntityIdCopy(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, TooltipDisplay display, Consumer<Component> components, TooltipFlag pIsAdvanced) {
        components.accept(Component.translatable("tooltips.touhou_little_maid.entity_id_copy.desc").withStyle(ChatFormatting.GRAY));
    }
}
