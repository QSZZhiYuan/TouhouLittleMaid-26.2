package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;

public abstract class TileEntityJoyRenderer<T extends TileEntityJoy> implements BlockEntityRenderer<T, MaidBlockEntityRenderState> {
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox(T te) {
        return RenderHelper.getAABB(te.getWorldPosition().offset(-2, 0, -2), te.getWorldPosition().offset(2, 1, 2));
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, MaidBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }
}
