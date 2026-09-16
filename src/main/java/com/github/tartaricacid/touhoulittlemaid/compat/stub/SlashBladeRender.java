package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;


public class SlashBladeRender {
    public static void renderSlashBlade(PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack) {
    }

    public static void renderMaidBackSlashBlade(PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack) {
    }

    public static void renderGeckoMaidBackSlashBlade(PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack) {
    }

    public static void renderMaidMainhandSlashBlade(Mob maid, BedrockModel<LivingEntityRenderState> model, PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack, float partialTicks) {
    }

    public static void renderMaidMainhandSlashBlade(LivingEntity maid, ILocationModel model, PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack, float partialTicks) {
    }

    public static void renderMaidOffhandSlashBlade(BedrockModel<?> model, PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack) {
    }

    public static void renderMaidOffhandSlashBlade(ILocationModel model, PoseStack matrixStack, SubmitNodeCollector bufferIn, int lightIn, ItemStack stack) {
    }
}