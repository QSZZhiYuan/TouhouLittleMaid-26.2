package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class MaidAreaRenderEvent {
    private static final Cache<Integer, SchedulePos> CACHE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent.AfterTranslucentBlocks event) {
    }

    public static void addSchedulePos(int id, SchedulePos pos) {
        CACHE.put(id, pos);
    }
}