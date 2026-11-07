package com.github.tartaricacid.touhoulittlemaid.client.animation.script;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GlWrapper {
    private static PoseStack poseStack;
    private static int matrixDepth;

    /** Scope legacy animation transforms to the pose currently being prepared for drawing. */
    public static void withPoseStack(PoseStack currentPose, Runnable animation) {
        PoseStack previousPose = poseStack;
        int previousDepth = matrixDepth;
        poseStack = currentPose;
        matrixDepth = 0;
        try {
            animation.run();
        } finally {
            while (matrixDepth > 0) {
                currentPose.popPose();
                matrixDepth--;
            }
            poseStack = previousPose;
            matrixDepth = previousDepth;
        }
    }

    public static void setPoseStack(PoseStack poseStackIn) {
        poseStack = poseStackIn;
        matrixDepth = 0;
    }

    public static void clearPoseStack() {
        poseStack = null;
        matrixDepth = 0;
    }

    public static void translate(double x, double y, double z) {
        poseStack.translate(x, y, z);
    }

    public static void rotate(double angle, double x, double y, double z) {
        Vector3f vector3f = new Vector3f(normalize(x), normalize(y), normalize(z));
        poseStack.mulPose(new Quaternionf().rotateAxis((float) angle * Mth.DEG_TO_RAD, vector3f));
    }

    public static void scale(double x, double y, double z) {
        poseStack.scale((float) x, (float) y, (float) z);
    }

    public static void pushMatrix() {
        poseStack.pushPose();
        matrixDepth++;
    }

    public static void popMatrix() {
        if (matrixDepth <= 0) {
            throw new IllegalStateException("Animation cannot pop the renderer's pose stack");
        }
        poseStack.popPose();
        matrixDepth--;
    }

    private static float normalize(double value) {
        if (value >= 1.0E-5) {
            return 1.0f;
        } else if (value <= -1.0E-5) {
            return -1.0f;
        } else {
            return 0;
        }
    }
}
