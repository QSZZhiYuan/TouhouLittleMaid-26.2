package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractModel<T extends EntityRenderState> extends EntityModel<T> {
    public AbstractModel(Function<Identifier, RenderType> pRenderType) {
        super(new ModelPart(java.util.List.of(), java.util.Map.of()), pRenderType);
    }

    public AbstractModel(ModelPart root) {
        super(root);
    }

    protected AbstractModel() {
        this(RenderTypes::entityCutout);
    }

    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
        this.renderToBuffer(poseStack, buffer, light, overlay, r, g, b, a);
    }
}
