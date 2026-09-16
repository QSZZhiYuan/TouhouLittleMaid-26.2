package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityDanmakuRenderer extends EntityRenderer<EntityDanmaku, EntityDanmakuRenderer.DanmakuRenderState> {
    private static final Identifier DANMAKU_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/danmaku.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(DANMAKU_TEXTURE);

    public static class DanmakuRenderState extends EntityRenderState {
        public int colorOrdinal;
        public int typeOrdinal;
        public float size;
    }

    public EntityDanmakuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public DanmakuRenderState createRenderState() {
        return new DanmakuRenderState();
    }

    @Override
    public void extractRenderState(EntityDanmaku entity, DanmakuRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.colorOrdinal = entity.getColor().ordinal();
        state.typeOrdinal = entity.getDanmakuType().ordinal();
        state.size = (float) entity.getDanmakuType().getSize();
    }

    private static void vertex(VertexConsumer bufferIn, Matrix4f pose, Matrix3f normal, double x, double y, double texU, double texV, int packedLight) {
        Vector3f vector3f = normal.transform(new Vector3f(0.0F, 1.0F, 0.0F));
        bufferIn.addVertex(pose, (float) x, (float) y, 0.0F).setColor(255, 255, 255, 255).setUv((float) texU, (float) texV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(vector3f.x(), vector3f.y(), vector3f.z());
    }

    @Override
    public void submit(DanmakuRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        // 材质宽度
        int width = 416;
        // 材质长度
        int length = 128;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double startU = 32 * state.colorOrdinal;
        double startV = 32 * state.typeOrdinal;
        float size = state.size;

        poseStack.pushPose();
        poseStack.translate(0, 0.1, 0);
        poseStack.mulPose(camera.orientation);

        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            vertex(buffer, poseMatrix, normalMatrix, -size, size, (startU + 0) / width, (startV + 0) / length, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, -size, -size, (startU + 0) / width, (startV + 32) / length, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, size, -size, (startU + 32) / width, (startV + 32) / length, state.lightCoords);
            vertex(buffer, poseMatrix, normalMatrix, size, size, (startU + 32) / width, (startV + 0) / length, state.lightCoords);
        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }


    public Identifier getTextureLocation(DanmakuRenderState state) {
        return DANMAKU_TEXTURE;
    }
}
