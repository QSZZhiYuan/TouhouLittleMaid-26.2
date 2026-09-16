package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class EntityExtinguishingAgentRenderer extends EntityRenderer<EntityExtinguishingAgent, EntityRenderState> {
    public EntityExtinguishingAgentRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }


    @Nullable
    public Identifier getTextureLocation(EntityRenderState state) {
        return null;
    }
}
