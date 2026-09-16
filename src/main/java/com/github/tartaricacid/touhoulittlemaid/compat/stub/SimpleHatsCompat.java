package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.ILocationBone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SimpleHatsCompat {
    public static void init() {
    }

    public static void renderHat(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, Mob mob, ItemStack stack, BedrockModel<LivingEntityRenderState> model) {
    }

    public static void renderGeckoHat(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, Mob mob, ItemStack stack, List<? extends ILocationBone> model) {
    }
}