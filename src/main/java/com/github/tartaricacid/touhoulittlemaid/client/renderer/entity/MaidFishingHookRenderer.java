package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.compat.oculus.OculusCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MaidFishingHookRenderer<T extends MaidFishingHook> extends EntityRenderer<T, MaidFishingHookRenderer.MaidFishingHookRenderState> {
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(TEXTURE_LOCATION);

    public static class MaidFishingHookRenderState extends EntityRenderState {
        public boolean hasMaid;
        public float lineX;
        public float lineY;
        public float lineZ;
        public float lineR;
        public float lineG;
        public float lineB;
        public boolean oculusInstalled;
    }

    public MaidFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public MaidFishingHookRenderState createRenderState() {
        return new MaidFishingHookRenderState();
    }

    @Override
    public void extractRenderState(T entity, MaidFishingHookRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        EntityMaid maid = entity.getMaidOwner();
        if (maid == null) {
            state.hasMaid = false;
            return;
        }
        state.hasMaid = true;
        state.oculusInstalled = OculusCompat.isOculusInstalled();

        float lerpBodyRot;
        if (maid.getVehicle() instanceof LivingEntity vehicle) {
            lerpBodyRot = Mth.lerp(partialTicks, vehicle.yBodyRotO, vehicle.yBodyRot) * ((float) Math.PI / 180F);
        } else {
            lerpBodyRot = Mth.lerp(partialTicks, maid.yBodyRotO, maid.yBodyRot) * ((float) Math.PI / 180F);
        }
        double sin = Mth.sin(lerpBodyRot);
        double cos = Mth.cos(lerpBodyRot);

        double x1 = Mth.lerp(partialTicks, maid.xo, maid.getX()) - cos * 0.35D - sin * 0.8D;
        double y1 = maid.yo + maid.getEyeHeight() + (maid.getY() - maid.yo) * partialTicks - 0.45D;
        double z1 = Mth.lerp(partialTicks, maid.zo, maid.getZ()) - sin * 0.35D + cos * 0.8D;

        double x2 = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double y2 = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.25D;
        double z2 = Mth.lerp(partialTicks, entity.zo, entity.getZ());

        state.lineX = (float) (x1 - x2);
        state.lineY = (float) (y1 - y2) - 0.1875F;
        state.lineZ = (float) (z1 - z2);

        float[] colors = getLineColor(entity);
        state.lineR = colors[0];
        state.lineG = colors[1];
        state.lineB = colors[2];
    }

    protected float[] getLineColor(T fishingHook) {
        return new float[]{0f, 0f, 0f};
    }

    @Override
    public void submit(MaidFishingHookRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.hasMaid) {
            return;
        }
        poseStack.pushPose();

        // renderBobber
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(camera.orientation);
        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1);
            vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1);
            vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0);
            vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0);
        });
        poseStack.popPose();

        // renderFishingLine
        float x = state.lineX;
        float y = state.lineY;
        float z = state.lineZ;
        float r = state.lineR;
        float g = state.lineG;
        float b = state.lineB;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
            for (int i = 0; i <= 16; ++i) {
                stringVertex(x, y, z, buffer, pose, fraction(i), fraction(i + 1), r, g, b);
            }
            if (state.oculusInstalled) {
                buffer.addVertex(0.0f, 0.0f, 0.0f).setColor(0, 0, 0, 255).setNormal(0.0F, 0.0F, 0.0F);
            }
        });

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected float fraction(int numerator) {
        return (float) numerator / (float) 16;
    }

    protected void vertex(VertexConsumer consumer, PoseStack.Pose pose, int lightMapUV, float pX, int pY, int pU, int pV) {
        consumer.addVertex(pose, pX - 0.5F, pY - 0.5F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv((float) pU, (float) pV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightMapUV)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    protected void stringVertex(float pX, float pY, float pZ, VertexConsumer consumer, PoseStack.Pose pose, float fraction1, float fraction2, float r, float g, float b) {
        float x = pX * fraction1;
        float y = pY * (fraction1 * fraction1 + fraction1) * 0.5F + 0.25F;
        float z = pZ * fraction1;

        float nx = pX * fraction2 - x;
        float ny = pY * (fraction2 * fraction2 + fraction2) * 0.5F + 0.25F - y;
        float nz = pZ * fraction2 - z;
        float sqrt = Mth.sqrt(nx * nx + ny * ny + nz * nz);

        nx /= sqrt;
        ny /= sqrt;
        nz /= sqrt;

        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, 1.0F)
                .setNormal(pose, nx, ny, nz);
    }

    public Identifier getTextureLocation(T pEntity) {
        return TEXTURE_LOCATION;
    }
}
