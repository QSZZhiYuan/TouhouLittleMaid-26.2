package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ItemAdvancementIcon extends Item {
    public ItemAdvancementIcon(Item.Properties properties) {
        super(properties.stacksTo(1).overrideDescription("item.touhou_little_maid.advancement_icon"));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, TooltipDisplay display, Consumer<Component> components, TooltipFlag pIsAdvanced) {
        components.accept(Component.translatable("tooltips.touhou_little_maid.advancement_icon.desc").withStyle(ChatFormatting.GRAY));
    }
}
