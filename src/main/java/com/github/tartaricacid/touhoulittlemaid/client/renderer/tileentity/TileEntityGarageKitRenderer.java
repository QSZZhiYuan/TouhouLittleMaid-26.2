package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.render.MaidRenderState;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.STATUE_BASE;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityGarageKitRenderer implements BlockEntityRenderer<TileEntityGarageKit, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/statue_base.png");
    private SimpleBedrockModel<EntityRenderState> BASE_MODEL;

    public TileEntityGarageKitRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityGarageKit blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityGarageKit te = (TileEntityGarageKit) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        this.BASE_MODEL = BedrockModelLoader.getModel(STATUE_BASE);
        if (this.BASE_MODEL != null) {
            poseStack.pushPose();
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(1, 1.5, 1);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            RenderHelper.renderModelToBuffer(BASE_MODEL, poseStack, bufferIn, RenderTypes.entityCutout(TEXTURE), combinedLightIn, combinedOverlayIn, -1);
            poseStack.popPose();
        }

        CompoundTag data = te.getExtraData();
        Level world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return;
        }

        Identifier id = Identifier.tryParse(data.getStringOr("id", ""));
        if (id != null) {
            BuiltInRegistries.ENTITY_TYPE.getOptional(id).ifPresent(type -> {
                    try {
                        renderEntity(te, poseStack, bufferIn, combinedLightIn, data, world, type, camera);
                    } catch (ExecutionException e) {
                        TouhouLittleMaid.LOGGER.error("Failed to render garage kit entity", e);
                    }
                }
            );
        }
    }

    private void renderEntity(TileEntityGarageKit te, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, CompoundTag data, Level world, EntityType<?> type, CameraRenderState camera) throws ExecutionException {
        Entity entity;
        if (type.equals(InitEntities.MAID.get())) {
            long posId = te.getBlockPos().asLong();
            entity = EntityCacheUtil.STATUE_CACHE.get(posId, () -> {
                EntityMaid m = new EntityMaid(world);
                m.setId(-1);
                return m;
            });
        } else {
            entity = EntityCacheUtil.ENTITY_CACHE.get(type, () -> {
                Entity e = type.create(world, EntitySpawnReason.NATURAL);
                Entity result = Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
                result.setId(-1);
                return result;
            });
        }

        entity.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), data));
        if (entity instanceof EntityMaid maid) {
            clearMaidDataResidue(maid, true);
            maid.renderState = MaidRenderState.GARAGE_KIT;
            if (YsmCompat.isInstalled() && maid.isYsmModel()) {
                maid.tickCount = (int) world.getGameTime();
            } else {
                maid.tickCount = 0;
            }
        }


        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 0.21328125, 1);
        switch (te.getFacing()) {
            case EAST:
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case WEST:
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case SOUTH:
                break;
            case NORTH:
            default:
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
        }
        EntityRenderDispatcher render = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderState entityState = render.extractEntity(entity, 0);
        render.submit(entityState, camera, 0, 0, 0, poseStack, bufferIn);
        poseStack.popPose();
    }
}
