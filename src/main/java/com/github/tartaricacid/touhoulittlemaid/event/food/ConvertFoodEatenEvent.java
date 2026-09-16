package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseRemainder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

@EventBusSubscriber
public class ConvertFoodEatenEvent {
    @SubscribeEvent
    public static void onAfterMaidEat(MaidAfterEatEvent event) {
        ItemStack foodAfterEat = event.getFoodAfterEat();
        EntityMaid maid = event.getMaid();
        if (!foodAfterEat.isEmpty()) {
            UseRemainder useRemainder = foodAfterEat.get(DataComponents.USE_REMAINDER);
            if (useRemainder != null) {
                ItemStack convertedStack = useRemainder.convertInto().create();
                if (!convertedStack.isEmpty()) {
                    CombinedInvWrapper availableInv = maid.getAvailableInv(false);
                    ItemStack result = ItemHandlerHelper.insertItemStacked(availableInv, convertedStack, false);
                    // 如果女仆背包满了，掉落在地上
                    if (!result.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), convertedStack);
                        maid.level.addFreshEntity(itemEntity);
                    }
                }
            }
        }
    }
}
