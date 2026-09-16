package com.github.tartaricacid.touhoulittlemaid.compat.simplehats;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.ILocationBone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.List;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SimpleHatsCompat {
    private static final String SIMPLE_HATS = "simplehats";
    private static boolean isLoaded = false;

    public static void init() {
        isLoaded = ModList.get().isLoaded(SIMPLE_HATS);
    }

    public static void renderHat(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, Mob mob, ItemStack stack, BedrockModel<LivingEntityRenderState> model) {
        if (isLoaded) {
            SimpleHatsRenderer.renderHat(poseStack, bufferIn, packedLightIn, mob, stack, model);
        }
    }

    public static void renderGeckoHat(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, Mob mob, ItemStack stack, List<? extends ILocationBone> model) {
        if (isLoaded) {
            SimpleHatsRenderer.renderGeckoHat(poseStack, bufferIn, packedLightIn, mob, stack, model);
        }
    }
}
