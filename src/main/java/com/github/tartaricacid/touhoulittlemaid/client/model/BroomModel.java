package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.io.InputStream;

public class BroomModel extends SimpleBedrockModel<LivingEntityRenderState> {
    private final BedrockPart all;

    public BroomModel(InputStream stream) {
        super(stream);
        this.all = this.getPart("all");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
    }
}
