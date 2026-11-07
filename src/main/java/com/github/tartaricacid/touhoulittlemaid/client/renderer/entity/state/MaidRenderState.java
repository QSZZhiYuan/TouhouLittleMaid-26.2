package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MaidRenderState extends LivingEntityRenderState {
    public LivingEntity entity;
    public float attackTime;
    public boolean riding;
    public float limbSwing;
    public float limbSwingAmount;
    public float netHeadYaw;
    public float headPitch;
    public MaidModelInfo info;
    public List<Object> animations;
    public String modelId;
    public BedrockModel<LivingEntityRenderState> model;
}
