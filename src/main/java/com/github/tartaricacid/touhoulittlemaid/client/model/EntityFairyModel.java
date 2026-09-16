package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

import java.io.InputStream;

public class EntityFairyModel extends SimpleBedrockModel<LivingEntityRenderState> {
    private final BedrockPart head;
    private final BedrockPart armRight;
    private final BedrockPart armLeft;
    private final BedrockPart legLeft;
    private final BedrockPart legRight;
    private final BedrockPart wingLeft;
    private final BedrockPart wingRight;
    private final BedrockPart blink;

    public EntityFairyModel(InputStream stream) {
        super(stream);
        this.head = this.getPart("head");
        this.armRight = this.getPart("armRight");
        this.armLeft = this.getPart("armLeft");
        this.legLeft = this.getPart("legLeft");
        this.legRight = this.getPart("legRight");
        this.wingLeft = this.getPart("wingLeft");
        this.wingRight = this.getPart("wingRight");
        this.blink = this.getPart("blink");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        float ageInTicks = state.ageInTicks;
        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;
        armLeft.zRot = Mth.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -Mth.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        legLeft.xRot = Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
        legRight.xRot = -Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
        armLeft.xRot = -Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
        armRight.xRot = Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
        wingLeft.yRot = -Mth.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
        wingRight.yRot = Mth.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.visible = (55 < remainder && remainder < 60);
    }
}
