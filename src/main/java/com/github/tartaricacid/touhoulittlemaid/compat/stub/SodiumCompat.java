package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class SodiumCompat {
    public static void init() {
    }

    public static boolean isSodiumInstalled() {
        return false;
    }

    public static boolean sodiumRenderCubesOfBone(AnimatedGeoBone bone, PoseStack poseStack, VertexConsumer buffer, int cubePackedLight,
                                                  int packedOverlay, float red, float green, float blue, float alpha) {
        return false;
    }
}