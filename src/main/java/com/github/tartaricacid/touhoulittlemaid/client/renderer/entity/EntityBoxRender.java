package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.stream.IntStream;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.CAKE_BOX;

public class EntityBoxRender extends EntityRenderer<EntityBox, EntityBoxRender.BoxRenderState> {
    private final List<Identifier> texturesGroup = Lists.newArrayList();

    public static class BoxRenderState extends EntityRenderState {
        public int textureIndex;
    }

    public EntityBoxRender(EntityRendererProvider.Context manager) {
        super(manager);
        IntStream.range(0, EntityBox.MAX_TEXTURE_SIZE).forEach(this::addBoxTexture);
    }

    @Override
    public BoxRenderState createRenderState() {
        return new BoxRenderState();
    }

    @Override
    public void extractRenderState(EntityBox entity, BoxRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.textureIndex = entity.getTextureIndex();
    }

    @Override
    public void submit(BoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        SimpleBedrockModel<EntityRenderState> boxModel = BedrockModelLoader.getModel(CAKE_BOX);
        if (boxModel == null) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0, -1.501, 0.0);
        boxModel.setupAnim(state);
        submitNodeCollector.submitModel(
            boxModel, state, poseStack, RenderTypes.entityCutout(getTextureLocation(state)),
            state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null
        );
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }


    public Identifier getTextureLocation(BoxRenderState state) {
        return texturesGroup.get(state.textureIndex);
    }

    private void addBoxTexture(int index) {
        String fileName = String.format("textures/bedrock/entity/cake_box/cake_box_%s.png", index);
        texturesGroup.add(Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, fileName));
    }
}
