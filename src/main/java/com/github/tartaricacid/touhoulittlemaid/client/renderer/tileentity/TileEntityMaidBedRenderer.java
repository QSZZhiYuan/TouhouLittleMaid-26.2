package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBed;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;

import java.util.function.Function;

public class TileEntityMaidBedRenderer implements BlockEntityRenderer<TileEntityMaidBed, MaidBlockEntityRenderState> {
    private final Function<DyeColor, SimpleBedrockModel<?>> cacheModel = Util.memoize(color -> {
        Identifier id = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "bedrock/block/maid_bed/" + color.getName());
        return BedrockModelLoader.getModel(id);
    });
    private final Function<DyeColor, Identifier> cacheTexture = Util.memoize(color ->
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/maid_bed/" + color.getName() + ".png"));

    public TileEntityMaidBedRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityMaidBed blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector buffer, CameraRenderState camera) {
        TileEntityMaidBed bed = (TileEntityMaidBed) state.blockEntity;
        int packedLight = state.lightCoords;
        int packedOverlay = OverlayTexture.NO_OVERLAY;
        DyeColor dyeColor = bed.getColor();
        SimpleBedrockModel<?> model = cacheModel.apply(dyeColor);
        Identifier texture = cacheTexture.apply(dyeColor);

        poseStack.pushPose();
        int rotation = bed.getBlockState().getValue(HorizontalDirectionalBlock.FACING).get2DDataValue();
        poseStack.rotateAround(Axis.YN.rotationDegrees(rotation * 90), 0.5f, 0, 0.5f);
        poseStack.translate(0.5, 1.5, -0.5);
        poseStack.scale(-1, -1, 1);
        if (dyeColor == DyeColor.BLUE) {
            RenderHelper.renderModelToBuffer(model, poseStack, buffer, RenderTypes.entityTranslucent(texture), packedLight, packedOverlay);
        } else {
            RenderHelper.renderModelToBuffer(model, poseStack, buffer, RenderTypes.entityCutout(texture), packedLight, packedOverlay);
        }
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityMaidBed blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return RenderHelper.getAABB(pos.offset(-2, 0, -2), pos.offset(2, 1, 2));
    }
}
