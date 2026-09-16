package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockPicnicMat;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PicnicMatRender implements BlockEntityRenderer<TileEntityPicnicMat, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/picnic_mat.png");
    private SimpleBedrockModel<EntityRenderState> model;
    private final ItemModelResolver itemModelResolver;

    public PicnicMatRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityPicnicMat blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityPicnicMat picnicMat = (TileEntityPicnicMat) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        BlockState blockState = picnicMat.getBlockState();
        if (this.model == null) {
            this.model = BedrockModelLoader.getModel(BedrockModelLoader.PICNIC_MAT);
        }
        if (blockState.getValue(BlockPicnicMat.PART).isCenter() && this.model != null) {
            Direction facing = blockState.getValue(BlockPicnicMat.FACING);
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.mulPose(Axis.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
            {
                this.model.getPart("basketHide").visible = !picnicMat.isEmpty(0);
                this.model.getPart("breadHide").visible = !picnicMat.isEmpty(1);
                this.model.getPart("cakeHide").visible = !picnicMat.isEmpty(2);
            }
            {
                renderFood(picnicMat, 3, -0.6f, -1.5f, 1.4125f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 4, 0.15f, -1.2f, 1.4125f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 5, 0.55f, -1.6f, 1.4125f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);

                renderFood(picnicMat, 6, -0.5f, 1.65f, 1.4125f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 7, 0.375f, 1.575f, 1.4125f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 8, -0.05f, 1.2f, 1.25f, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
            }
            RenderHelper.renderModelToBuffer(model, poseStack, bufferIn, RenderTypes.entityCutout(TEXTURE), combinedLightIn, combinedOverlayIn, -1);
            poseStack.popPose();
        }
    }

    private void renderFood(TileEntityPicnicMat picnicMat, int slotId, float x, float y, float z, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack storageItem = picnicMat.getStorageItem(slotId);
        if (!storageItem.isEmpty()) {
            int count = storageItem.getCount();
            poseStack.pushPose();
            poseStack.mulPose(Axis.XN.rotationDegrees(90));
            poseStack.translate(x, y, z);
            poseStack.scale(0.4f, 0.4f, 0.4f);
            renderItemStatic(storageItem, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, picnicMat.getLevel());
            if (count >= 10) {
                int stackCount = count / 10;
                for (int i = 0; i < stackCount; i++) {
                    poseStack.translate(Math.sin(i) * 0.05, Math.cos(i) * 0.03, -0.07);
                    poseStack.mulPose(Axis.ZN.rotationDegrees((float) Math.cos(i) * 60));
                    renderItemStatic(storageItem, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, picnicMat.getLevel());
                }
            }
            poseStack.popPose();
        }
    }

    private void renderItemStatic(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn, net.minecraft.world.level.Level level) {
        ItemStackRenderState itemState = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, level, null, 0);
        itemState.submit(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, -1);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityPicnicMat blockEntity) {
        BlockState blockState = blockEntity.getBlockState();
        BlockPos pos = blockEntity.getBlockPos();
        if (blockState.getValue(BlockPicnicMat.PART).isCenter()) {
            return RenderHelper.getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
        }
        return new AABB(pos);
    }
}
