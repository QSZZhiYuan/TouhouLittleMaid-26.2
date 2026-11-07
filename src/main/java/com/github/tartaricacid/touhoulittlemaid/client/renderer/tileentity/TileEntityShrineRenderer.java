package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityShrine;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TileEntityShrineRenderer implements BlockEntityRenderer<TileEntityShrine, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/shrine.png");
    private SimpleBedrockModel<EntityRenderState> model;
    private final ItemModelResolver itemModelResolver;

    public TileEntityShrineRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityShrine blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityShrine shrine = (TileEntityShrine) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        Direction facing = shrine.getBlockState().getValue(BlockGomoku.FACING);
        this.model = BedrockModelLoader.getModel(BedrockModelLoader.SHRINE);
        if (this.model != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.mulPose(Axis.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
            RenderHelper.renderModelToBuffer(model, poseStack, bufferIn, RenderTypes.entityCutout(TEXTURE), combinedLightIn, combinedOverlayIn, -1);
            poseStack.popPose();
        }

        Level level = shrine.getLevel();
        if (level == null) {
            return;
        }
        ItemStack stack = shrine.getStorageItem();
        poseStack.pushPose();
        poseStack.translate(0.5, 0.85, 0.5);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        float deg = (level.getGameTime() + state.partialTick) % 360;
        poseStack.mulPose(Axis.YN.rotationDegrees(deg));
        ItemStackRenderState itemState = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.GROUND, level, null, 0);
        itemState.submit(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, -1);
        poseStack.popPose();
    }
}
