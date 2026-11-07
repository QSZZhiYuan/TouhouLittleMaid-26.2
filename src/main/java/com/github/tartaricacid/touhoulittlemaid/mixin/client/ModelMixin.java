package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Model.class)
public class ModelMixin {
    @Inject(method = "renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", at = @At("HEAD"), cancellable = true, require = 1)
    private <S> void onRenderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color, CallbackInfo ci) {
        if ((Object) this instanceof AbstractBedrockEntityModel<?> bedrockModel) {
            bedrockModel.renderBedrockToBuffer(poseStack, buffer, lightCoords, overlayCoords, color);
            ci.cancel();
        } else if ((Object) this instanceof AbstractBedrockModel bedrockModel) {
            bedrockModel.renderBedrockToBuffer(poseStack, buffer, lightCoords, overlayCoords, color);
            ci.cancel();
        }
    }
}
