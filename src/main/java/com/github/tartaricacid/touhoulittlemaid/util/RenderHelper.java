package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.BedrockModelProvider;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class RenderHelper {
    public static void renderModelToBuffer(Model<?> model, PoseStack poseStack, SubmitNodeCollector bufferIn, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor) {
        if (model instanceof BedrockModelProvider<?> bedrock) {
            // Submission is deferred. Subsequent entities/blocks can animate or hide these shared bones before drawing.
            var parts = bedrock.getModelMap().values().stream().filter(part -> part.parent == null)
                    .map(BedrockPart::copyForRender).toList();
            float red = ((tintedColor >>> 16) & 255) / 255.0f;
            float green = ((tintedColor >>> 8) & 255) / 255.0f;
            float blue = (tintedColor & 255) / 255.0f;
            float alpha = ((tintedColor >>> 24) & 255) / 255.0f;
            bufferIn.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
                PoseStack ps = new PoseStack();
                ps.last().set(pose);
                for (BedrockPart part : parts) {
                    part.render(ps, vertexConsumer, lightCoords, overlayCoords, red, green, blue, alpha);
                }
            });
            return;
        }
        bufferIn.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
            PoseStack ps = new PoseStack();
            ps.last().set(pose);
            model.renderToBuffer(ps, vertexConsumer, lightCoords, overlayCoords, tintedColor);
        });
    }

    public static void renderModelToBuffer(Model<?> model, PoseStack poseStack, SubmitNodeCollector bufferIn, RenderType renderType, int lightCoords, int overlayCoords) {
        renderModelToBuffer(model, poseStack, bufferIn, renderType, lightCoords, overlayCoords, -1);
    }

    public static void renderFloatingText(PoseStack poseStack, String text, Vec3 vec, int color, float scale, float yOffset) {
        renderFloatingText(poseStack, text, vec.x + 0.5, vec.y + 1.5, vec.z + 0.5, color, scale, true, yOffset, true);
    }

    public static void renderFloatingText(PoseStack poseStack, String text, double x, double y, double z, int color, float scale, boolean center, float yOffset, boolean seeThrough) {
        // TODO: 26.2 渲染系统重构，renderBuffers/drawInBatch/endBatch 已移除，浮动文本渲染暂时禁用
    }

    public static void renderLine(PoseStack poseStack, VertexConsumer consumer, Vec3 start, Vec3 end, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f, 1.0F, 0.0F, 0.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f, 1.0F, 0.0F, 0.0F);

        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f, 0.0F, 1.0F, 0.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f, 0.0F, 1.0F, 0.0F);

        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f, 0.0F, 0.0F, 1.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f, 0.0F, 0.0F, 1.0F);
    }

    public static void renderCylinder(PoseStack poseStack, VertexConsumer consumer, Vec3 centerPos, double radius, int precision, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = precision; i >= 0; i--) {
            double x1 = centerPos.x + radius * Math.cos(i * precisionAngle);
            double x2 = centerPos.x + radius * Math.cos((i - 1) * precisionAngle);
            double y = centerPos.y;
            double z1 = centerPos.z + radius * Math.sin(i * precisionAngle);
            double z2 = centerPos.z + radius * Math.sin((i - 1) * precisionAngle);
            addVertex(consumer, matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f, 1.0F, 0.0F, 0.0F);
            addVertex(consumer, matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f, 1.0F, 0.0F, 0.0F);

            addVertex(consumer, matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f, 0.0F, 1.0F, 0.0F);
            addVertex(consumer, matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f, 0.0F, 1.0F, 0.0F);

            addVertex(consumer, matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f, 0.0F, 0.0F, 1.0F);
            addVertex(consumer, matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f, 0.0F, 0.0F, 1.0F);
        }
    }

    public static void addVertex(VertexConsumer consumer, Matrix4f matrix4f, float x, float y, float z, float red, float green, float blue, float alpha, Matrix3f matrix3f, float normalX, float normalY, float normalZ) {
        Vector3f vector3f = matrix3f.transform(new Vector3f(normalX, normalY, normalZ));
        consumer.addVertex(matrix4f, x, y, z).setColor(red, green, blue, alpha).setNormal(vector3f.x(), vector3f.y(), vector3f.z());
    }

    public static AABB getAABB(BlockPos pStart, BlockPos pEnd) {
        return new AABB(pStart.getX(), pStart.getY(), pStart.getZ(), pEnd.getX(), pEnd.getY(), pEnd.getZ());
    }
}
