package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.backpack.BackpackClientHelper;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

public class GeckoLayerMaidBackpack<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    public GeckoLayerMaidBackpack(R entityRendererIn) {
        super(entityRendererIn);
    }

    protected static void renderColoredCutoutModel(net.minecraft.client.model.EntityModel<?> pModel, Identifier pTextureLocation, PoseStack pPoseStack, SubmitNodeCollector pBuffer, int pPackedLight) {
        pBuffer.submitCustomGeometry(pPoseStack, RenderTypes.entityCutout(pTextureLocation), (pose, buffer) -> {
            pModel.renderToBuffer(new PoseStack(), buffer, pPackedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, -1);
        });
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBackpack<>(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null) {
            return;
        }
        ILocationModel model = getLocationModel(entity);
        if (model != null) {
            if (!getGeoEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }
            if (!model.backpackBones().isEmpty()) {
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, model.backpackBones());
                poseStack.translate(0, 1, 0.25);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                boolean showBackpack = entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack();
                IMaidBackpack backpack = showBackpack ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
                BackpackClientHelper.findBackpackModel(backpack.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), poseStack, buffer, packedLight));
                poseStack.popPose();
            }
        }
    }
}
