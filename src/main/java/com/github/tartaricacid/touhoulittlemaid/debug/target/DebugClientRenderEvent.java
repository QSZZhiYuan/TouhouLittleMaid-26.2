package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.util.VisibleForDebug;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@VisibleForDebug
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class DebugClientRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        if (TouhouLittleMaid.DEBUG) {
            // Debug rendering API has changed significantly in 26.2
            // The pathfinding renderer and buffer source APIs are no longer directly accessible
        }
    }
}
