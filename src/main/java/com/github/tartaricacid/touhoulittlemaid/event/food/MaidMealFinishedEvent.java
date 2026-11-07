package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

/** Food consumption moved out of LivingEntity.eat into item components in modern Minecraft. */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidMealFinishedEvent {
    @SubscribeEvent
    public static void onFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof EntityMaid maid && !maid.level().isClientSide()
                && event.getItem().has(DataComponents.FOOD)) {
            ItemStack food = event.getItem();
            ItemStack result = event.getResultStack();
            // Preserve the existing event contract: remaining food, or the last consumed food if empty/converted.
            ItemStack foodAfterEat = ItemStack.isSameItemSameComponents(food, result)
                    ? result.copy() : food.copyWithCount(1);
            NeoForge.EVENT_BUS.post(new MaidAfterEatEvent(maid, foodAfterEat, true));
        }
    }
}
