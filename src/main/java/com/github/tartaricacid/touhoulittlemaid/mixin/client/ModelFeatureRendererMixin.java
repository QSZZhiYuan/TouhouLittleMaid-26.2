package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** The 26.2 model pass runs after entity submission, when the entity pose stack is no longer valid. */
@Mixin(ModelFeatureRenderer.class)
public abstract class ModelFeatureRendererMixin {
    @Shadow @Final private PoseStack poseStack;

    @Redirect(method = "prepareModel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V"), require = 1)
    private <S> void tlm$animateAtSubmittedPose(Model<S> model, S state) {
        if (model instanceof BedrockModel<?>) {
            GlWrapper.withPoseStack(this.poseStack, () -> model.setupAnim(state));
        } else {
            model.setupAnim(state);
        }
    }
}
