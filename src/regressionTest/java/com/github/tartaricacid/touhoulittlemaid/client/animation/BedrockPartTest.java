package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class BedrockPartTest {
    @Test
    public void snapshotDoesNotFollowLaterEntityAnimations() {
        BedrockPart root = new BedrockPart();
        BedrockPart child = new BedrockPart();
        root.addChild(child);
        child.xScale = 2;
        child.xRot = 0.5f;
        BedrockPart snapshot = root.copyForRender();
        root.visible = false;
        child.xScale = 0;
        child.xRot = 8;
        assertTrue(snapshot.visible);
        assertEquals(2, snapshot.children.getFirst().xScale, 0.0001);
        assertEquals(0.5, snapshot.children.getFirst().xRot, 0.0001);
        assertSame(snapshot, snapshot.children.getFirst().parent);
    }

    @Test
    public void restoresVisibilityScaleAndRotationBetweenEntities() {
        BedrockPart part = new BedrockPart();
        part.setPos(1, 2, 3);
        part.xRot = 0.75f;
        ModelRendererWrapper wrapper = new ModelRendererWrapper(part);
        part.visible = false;
        part.xScale = 0;
        part.xRot = 3;
        part.x = 30;
        part.offsetX = 99;
        wrapper.resetPose();
        assertTrue(part.visible);
        assertEquals(1, part.xScale, 0.0001);
        assertEquals(0.75, part.xRot, 0.0001);
        assertEquals(1, part.x, 0.0001);
        assertEquals(0, part.offsetX, 0.0001);
    }
}
