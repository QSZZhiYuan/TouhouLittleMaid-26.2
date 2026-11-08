package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.io.InputStream;

public class EntityBoxModel extends SimpleBedrockModel<EntityRenderState> {
    private final BedrockPart x1;
    private final BedrockPart x2;
    private final BedrockPart z1;
    private final BedrockPart z2;
    private final BedrockPart top;

    public EntityBoxModel(InputStream stream) {
        super(stream);
        this.x1 = this.getPart("x1");
        this.x2 = this.getPart("x2");
        this.z1 = this.getPart("z1");
        this.z2 = this.getPart("z2");
        this.top = this.getPart("top");
    }

    @Override
    public void setupAnim(EntityRenderState state) {
    }
}
