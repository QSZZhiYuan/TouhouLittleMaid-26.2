package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class GunClientUtil {
    public static void renderBackGun(ItemStack offhandItem, ILocationModel geoModel, IMaid maid, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLight) {
    }

    public static void renderBackGun(PoseStack matrixStack, SubmitNodeCollector bufferIn, int packedLightIn, ItemStack stack, IMaid maid) {
    }

    public static void addItemTranslate(PoseStack poseStack, ItemStack itemStack, boolean isLeft) {
    }

    public static boolean onHoldGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
        return false;
    }

    @Nullable
    public static PlayState playGunMainAnimation(IMaid maid, AnimationEvent<GeckoMaidEntity<?>> event, String animationName, ILoopType loopType) {
        return null;
    }

    @Nullable
    public static PlayState playGunHoldAnimation(ItemStack mainHandItem, AnimationEvent<GeckoMaidEntity<?>> event) {
        return null;
    }
}