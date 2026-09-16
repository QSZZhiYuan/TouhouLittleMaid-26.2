package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityPowerPointRenderer extends EntityRenderer<EntityPowerPoint, EntityPowerPointRenderer.PowerPointRenderState> {
    private static final Identifier POWER_POINT_TEXTURES = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(POWER_POINT_TEXTURES);

    public static class PowerPointRenderState extends EntityRenderState {
        public int icon;
    }

    public EntityPowerPointRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public PowerPointRenderState createRenderState() {
        return new PowerPointRenderState();
    }

    @Override
    public void extractRenderState(EntityPowerPoint entity, PowerPointRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.icon = entity.getIcon();
    }

    private static void vertex(VertexConsumer bufferIn, Matrix4f pose, Matrix3f normal, double x, double y, double texU, double texV, int packedLight) {
        Vector3f vector3f = normal.transform(new Vector3f(0.0F, 1.0F, 0.0F));
        bufferIn.addVertex(pose, (float) x, (float) y, 0.0F).setColor(255, 255, 255, 128).setUv((float) texU, (float) texV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(vector3f.x(), vector3f.y(), vector3f.z());
    }

    @Override
    public void submit(PowerPointRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        int texIndex = state.icon;
        int remainder = texIndex % 4;
        int quotient = texIndex / 4;
        double texPos1 = remainder * 16 / 64.0;
        double texPos2 = (remainder * 16 + 16) / 64.0;
        double texPos3 = quotient * 16 / 64.0;
        double texPos4 = (quotient * 16 + 16) / 64.0;

        poseStack.pushPose();
        poseStack.translate(0, 0.1, 0);
        poseStack.mulPose(camera.orientation);
        poseStack.scale(0.3F, 0.3F, 0.3F);

        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            vertex(buffer, poseMatrix, normalMatrix, -1, -0.25, texPos1, texPos4, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, 1, -0.25, texPos2, texPos4, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, 1, 1.75, texPos2, texPos3, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, -1, 1.75, texPos1, texPos3, state.lightCoords);
        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public Identifier getTextureLocation(EntityPowerPoint entity) {
        return POWER_POINT_TEXTURES;
    }
}
