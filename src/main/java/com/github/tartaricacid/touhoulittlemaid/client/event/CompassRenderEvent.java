package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class CompassRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent.AfterTranslucentBlocks event) {
    }
}