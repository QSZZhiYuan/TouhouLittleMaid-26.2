package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class TileEntityAltarRenderer implements BlockEntityRenderer<TileEntityAltar, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/altar.png");
    private SimpleBedrockModel<? extends EntityRenderState> model;
    private final ItemModelResolver itemModelResolver;

    public TileEntityAltarRenderer(BlockEntityRendererProvider.Context render) {
        this.itemModelResolver = render.itemModelResolver();
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityAltar blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityAltar te = (TileEntityAltar) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        if (te.isRender()) {
            if (this.model == null) {
                this.model = BedrockModelLoader.getModel(BedrockModelLoader.ALTAR);
            }
            if (this.model != null) {
                poseStack.pushPose();
                this.setTranslateAndPose(te, poseStack);
                poseStack.mulPose(Axis.ZN.rotationDegrees(180));
                RenderHelper.renderModelToBuffer(model, poseStack, bufferIn, RenderTypes.entityTranslucent(TEXTURE), combinedLightIn, combinedOverlayIn, -1);
                poseStack.popPose();
            }
        }

        if (te.isCanPlaceItem() && !te.handler.getStackInSlot(0).isEmpty()) {
            ItemStack stack = te.handler.getStackInSlot(0);
            poseStack.pushPose();
            double time = (System.currentTimeMillis() + te.getBlockPos().asLong()) % 3600;
            poseStack.translate(0.5, 1.25 + Math.sin(time / 1800 * Math.PI) * 0.1, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) time / 10));
            ItemStackRenderState itemState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.GROUND, te.getLevel(), null, 0);
            itemState.submit(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, -1);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    private void setTranslateAndPose(TileEntityAltar te, PoseStack poseStack) {
        switch (te.getDirection()) {
            case SOUTH:
                poseStack.translate(1, -1.5, -3);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                poseStack.translate(-3, -1.5, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case WEST:
                poseStack.translate(4, -1.5, 1);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case NORTH:
            default:
                poseStack.translate(0, -1.5, 4);
        }
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityAltar te) {
        return RenderHelper.getAABB(te.getWorldPosition().offset(-9, -5, -9),
                te.getWorldPosition().offset(9, 5, 9));
    }
}
