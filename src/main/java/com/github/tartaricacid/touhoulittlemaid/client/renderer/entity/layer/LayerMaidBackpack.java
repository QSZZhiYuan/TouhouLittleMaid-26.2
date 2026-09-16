package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.backpack.BackpackClientHelper;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Mob;

import java.awt.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LayerMaidBackpack extends RenderLayer<MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackpack(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        BackpackClientHelper.init(modelSet);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, MaidRenderState state, float yRot, float xRot) {
        if (!(state.entity instanceof Mob mob)) return;
        EntityMaid maid = IMaid.convertToMaid(mob);
        if (maid == null || renderer.getMainInfo() == null) {
            return;
        }
        if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping() || mob.isInvisible()) {
            return;
        }
        // 稍微缩放，避免整数倍的 z-flight
        poseStack.scale(1.01f, 1.01f, 1.01f);
        // [-13, 41, 15]
        if (getParentModel().hasBackpackPositioningModel()) {
            BedrockPart renderer = getParentModel().getBackpackPositioningModel();
            poseStack.translate(renderer.x * 0.0625, 0.0625 * (renderer.y - 23 + 8), 0.0625 * (renderer.z + 4));
        } else {
            poseStack.translate(0, -0.5, 0.25);
        }
        IMaidBackpack type = maid.getConfigManager().isShowBackpack() ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
        BackpackClientHelper.findBackpackModel(type.getId()).ifPresent(pair -> renderColoredCutoutModel((Model<? super MaidRenderState>) pair.getLeft(), pair.getRight(), poseStack, bufferIn, packedLightIn, state, new Color(1.0f, 1.0f, 1.0f).getRGB(), 0));
    }
}
