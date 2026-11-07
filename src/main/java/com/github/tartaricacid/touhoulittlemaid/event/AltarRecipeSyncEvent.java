package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/** Custom recipes are no longer sent to clients automatically. */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class AltarRecipeSyncEvent {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(InitRecipes.ALTAR_CRAFTING.get());
    }
}
