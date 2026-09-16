package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.extended.LivingEntityRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity, EntityRenderState> implements LivingEntityRendererAccessor {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Unique
    @Override
    public void tlm$submitNameTag(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
