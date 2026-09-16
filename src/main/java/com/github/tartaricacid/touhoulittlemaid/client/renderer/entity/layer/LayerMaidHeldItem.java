package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.GunClientUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LayerMaidHeldItem extends RenderLayer<MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    @Nullable
    private final ItemInHandRenderer itemInHandRenderer;

    public LayerMaidHeldItem(EntityMaidRenderer maidRenderer, @Nullable ItemInHandRenderer pItemInHandRenderer) {
        super(maidRenderer);
        this.itemInHandRenderer = pItemInHandRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, MaidRenderState state, float yRot, float xRot) {
        if (!(state.entity instanceof Mob maid)) return;
        ItemStack mainRightItem = maid.getMainHandItem();
        ItemStack offLeftItem = maid.getOffhandItem();
        BedrockModel<LivingEntityRenderState> model = getParentModel();
        float partialTicks = state.partialTick;
        if (!mainRightItem.isEmpty() && model.hasRightArm() && !RenderFixer.isCarryOnRender(mainRightItem, bufferIn)) {
            if (SlashBladeCompat.isSlashBladeItem(mainRightItem)) {
                SlashBladeRender.renderMaidMainhandSlashBlade(maid, model, poseStack, bufferIn, packedLightIn, mainRightItem, partialTicks);
            } else {
                this.renderArmWithItem(maid, mainRightItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, bufferIn, packedLightIn);
            }
        }
        if (!offLeftItem.isEmpty() && model.hasLeftArm() && !RenderFixer.isCarryOnRender(offLeftItem, bufferIn)) {
            if (SlashBladeCompat.isSlashBladeItem(offLeftItem)) {
                SlashBladeRender.renderMaidOffhandSlashBlade(model, poseStack, bufferIn, packedLightIn, offLeftItem);
            } else {
                this.renderArmWithItem(maid, offLeftItem, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, bufferIn, packedLightIn);
            }
        }
    }

    private void renderArmWithItem(Mob maid, ItemStack itemStack, ItemDisplayContext transformTypeIn, HumanoidArm handSide, PoseStack poseStack, SubmitNodeCollector typeBuffer, int combinedLightIn) {
        if (!itemStack.isEmpty() && itemInHandRenderer != null) {
            poseStack.pushPose();
            boolean isLeft = handSide == HumanoidArm.LEFT;
            getParentModel().translateToHand(handSide, poseStack);
            if (getParentModel().hasArmPositioningModel(handSide)) {
                getParentModel().translateToPositioningHand(handSide, poseStack);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.translate(0, 0.125, -0.0625);
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.translate((isLeft ? -1 : 1) / 16.0, 0.125, -0.525);
            }
            GunClientUtil.addItemTranslate(poseStack, itemStack, isLeft);
            this.itemInHandRenderer.renderItem(maid, itemStack, transformTypeIn, poseStack, typeBuffer, combinedLightIn);
            poseStack.popPose();
        }
    }
}
