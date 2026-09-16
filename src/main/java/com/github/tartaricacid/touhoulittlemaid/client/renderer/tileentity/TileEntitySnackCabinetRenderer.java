package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockSnackCabinet;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntitySnackCabinet;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySnackCabinetRenderer implements BlockEntityRenderer<TileEntitySnackCabinet, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/snack_cabinet.png");
    private SimpleBedrockModel<EntityRenderState> model;
    private BedrockPart full;
    private BedrockPart half;

    public TileEntitySnackCabinetRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntitySnackCabinet blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector bufferIn, CameraRenderState camera) {
        if (this.model == null) {
            this.model = BedrockModelLoader.getModel(BedrockModelLoader.SNACK_CABINET);
            if (this.model != null) {
                this.full = this.model.getPart("full");
                this.half = this.model.getPart("half");
            }
        }
        if (this.model == null) {
            return;
        }
        TileEntitySnackCabinet cabinet = (TileEntitySnackCabinet) state.blockEntity;
        BlockState blockState = cabinet.getBlockState();
        Direction facing = blockState.getValue(BlockSnackCabinet.FACING);
        int type = blockState.getValue(BlockSnackCabinet.TYPE);

        if (type == BlockSnackCabinet.TYPE_FULL) {
            this.full.visible = true;
            this.half.visible = false;
        } else if (type == BlockSnackCabinet.TYPE_HALF) {
            this.full.visible = false;
            this.half.visible = true;
        } else {
            this.full.visible = false;
            this.half.visible = false;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
        RenderHelper.renderModelToBuffer(this.model, poseStack, bufferIn, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
