package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.lang.reflect.Method;

@EventBusSubscriber
public final class UseNameTagEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (stack.get(DataComponents.CUSTOM_NAME) == null && player.getMainHandItem().getItem() == Items.NAME_TAG && player.equals(maid.getOwner())) {
            if (player.level.isClientSide()) {
                try {
                    Class<?> helperClass = Class.forName("com.github.tartaricacid.touhoulittlemaid.client.gui.item.NameTagClientHelper");
                    Method method = helperClass.getMethod("openNameTagGui", EntityMaid.class);
                    method.invoke(null, maid);
                } catch (Exception ignore) {
                }
            }
            event.setCanceled(true);
        }
    }
}
