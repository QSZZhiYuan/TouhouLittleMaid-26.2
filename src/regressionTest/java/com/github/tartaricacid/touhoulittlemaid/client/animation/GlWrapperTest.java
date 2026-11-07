package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import org.junit.Test;

import static org.junit.Assert.*;

public class GlWrapperTest {
    @Test
    public void nestedRenderingRestoresTheOuterAnimationPose() {
        PoseStack outer = new PoseStack();
        PoseStack inner = new PoseStack();
        GlWrapper.withPoseStack(outer, () -> {
            GlWrapper.translate(1, 0, 0);
            GlWrapper.withPoseStack(inner, () -> GlWrapper.translate(9, 0, 0));
            GlWrapper.translate(2, 0, 0);
        });
        assertEquals(3, outer.last().pose().m30(), 0.0001);
        assertEquals(9, inner.last().pose().m30(), 0.0001);
    }

    @Test
    public void balancesPushesEvenWhenAnimationFails() {
        PoseStack pose = new PoseStack();
        try {
            GlWrapper.withPoseStack(pose, () -> {
                GlWrapper.pushMatrix();
                GlWrapper.translate(15, 0, 0);
                throw new IllegalStateException("broken animation");
            });
            fail();
        } catch (IllegalStateException expected) {
        }
        assertTrue(pose.isEmpty());
        assertEquals(0, pose.last().pose().m30(), 0.0001);
    }

    @Test
    public void animationCannotPopRendererOwnedPoses() {
        PoseStack pose = new PoseStack();
        try {
            GlWrapper.withPoseStack(pose, GlWrapper::popMatrix);
            fail();
        } catch (IllegalStateException expected) {
        }
        assertTrue(pose.isEmpty());
    }
}
