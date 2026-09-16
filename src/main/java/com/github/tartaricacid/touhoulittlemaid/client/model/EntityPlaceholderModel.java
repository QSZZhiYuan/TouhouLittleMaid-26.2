package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.BedrockModelUtil;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCubePerFace;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class EntityPlaceholderModel extends AbstractModel<EntityRenderState> {
    private final BedrockPart bone;

    public EntityPlaceholderModel() {
        bone = new BedrockPart();
        bone.setPos(8.0F, 24.0F, -10.0F);
        bone.cubes.add(new BedrockCubePerFace(-16.0F, -16.0F, 9.5F, 16.0F, 16.0F, 0, 0, 16, 16, BedrockModelUtil.singleSouthFace()));
    }

    @Override
    public void setupAnim(EntityRenderState state) {
    }
}
