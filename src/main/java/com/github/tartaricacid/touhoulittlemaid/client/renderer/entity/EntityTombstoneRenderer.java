package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;


import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.TOMBSTONE;

public class EntityTombstoneRenderer extends EntityRenderer<EntityTombstone, EntityTombstoneRenderer.TombstoneRenderState> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/tombstone/tombstone_overworld.png");
    private static final Identifier THE_NETHER_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/tombstone/tombstone_the_nether.png");
    private static final Identifier THE_END_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/tombstone/tombstone_the_end.png");
    private static final Identifier TWILIGHT_FOREST_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/tombstone/tombstone_twilight_forest.png");
    private static final Identifier AETHER_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/tombstone/tombstone_aether.png");

    private static final Identifier TWILIGHT_FOREST_LEVEL_ID = Identifier.fromNamespaceAndPath("twilightforest", "twilight_forest");
    private final static Identifier AETHER_LEVEL_ID = Identifier.fromNamespaceAndPath("aether", "the_aether");

    private static final int NAME_SHOW_DISTANCE = 64;

    public static class TombstoneRenderState extends EntityRenderState {
        public Identifier texture;
        public Component maidName;
        public boolean shouldShowName;
    }

    public EntityTombstoneRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public TombstoneRenderState createRenderState() {
        return new TombstoneRenderState();
    }

    @Override
    public void extractRenderState(EntityTombstone entity, TombstoneRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.texture = getTextureLocation(entity);
        state.maidName = entity.getMaidName();
        state.shouldShowName = !entity.getMaidName().equals(Component.empty());
    }

    @Override
    public void submit(TombstoneRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        SimpleBedrockModel<EntityRenderState> tombstoneModel = BedrockModelLoader.getModel(TOMBSTONE);
        if (tombstoneModel == null) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0, -1.501, 0.0);
        tombstoneModel.setupAnim(state);
        submitNodeCollector.submitModel(
            tombstoneModel, state, poseStack, RenderTypes.entityCutout(state.texture),
            state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null
        );
        poseStack.popPose();
        if (state.shouldShowName) {
            renderNameTag(state, Component.translatable("entity.touhou_little_maid.tombstone.display").withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE), 1.6f, poseStack, submitNodeCollector, camera, state.lightCoords);
            renderNameTag(state, state.maidName, 1.85f, poseStack, submitNodeCollector, camera, state.lightCoords);
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private Identifier getTextureLocation(EntityTombstone entity) {
        Identifier dimension = entity.level().dimension().identifier();
        if (dimension.equals(Level.NETHER.identifier())) {
            return THE_NETHER_TEXTURE;
        }
        if (dimension.equals(Level.END.identifier())) {
            return THE_END_TEXTURE;
        }
        if (dimension.equals(TWILIGHT_FOREST_LEVEL_ID)) {
            return TWILIGHT_FOREST_TEXTURE;
        }
        if (dimension.equals(AETHER_LEVEL_ID)) {
            return AETHER_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }

    private void renderNameTag(TombstoneRenderState state, Component component, float yOffset, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int packedLight) {
        if (state.distanceToCameraSq < (NAME_SHOW_DISTANCE * NAME_SHOW_DISTANCE)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, yOffset, 0.0F);
            poseStack.mulPose(camera.orientation);
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Font font = this.getFont();
            float width = (float) (-font.width(component) / 2);
            submitNodeCollector.submitText(poseStack, width, 0, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, packedLight, -1, 0, 0);
            poseStack.popPose();
        }
    }
}
