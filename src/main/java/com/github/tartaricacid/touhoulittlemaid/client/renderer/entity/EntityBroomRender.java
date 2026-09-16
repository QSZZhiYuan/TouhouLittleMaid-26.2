package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BroomModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.BROOM;

public class EntityBroomRender extends LivingEntityRenderer<EntityBroom, LivingEntityRenderState, BroomModel> {
    private static final Identifier BROOM_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/broom.png");

    public EntityBroomRender(EntityRendererProvider.Context context) {
        super(context, (BroomModel) BedrockModelLoader.<LivingEntityRenderState>getModel(BROOM), 0.5f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    protected boolean shouldShowName(EntityBroom entity, double distanceToCameraSq) {
        return super.shouldShowName(entity, distanceToCameraSq) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return BROOM_TEXTURE;
    }
}
