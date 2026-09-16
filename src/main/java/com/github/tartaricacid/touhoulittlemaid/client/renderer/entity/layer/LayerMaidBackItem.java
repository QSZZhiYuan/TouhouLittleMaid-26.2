package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.GunClientUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.backpack.BackpackClientHelper;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;


public class LayerMaidBackItem extends RenderLayer<MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackItem(EntityMaidRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void submit(PoseStack matrixStack, SubmitNodeCollector bufferIn, int packedLightIn, MaidRenderState state, float yRot, float xRot) {
        if (!(state.entity instanceof Mob mob)) return;
        IMaid maid = IMaid.convert(mob);
        if (maid == null || renderer.getMainInfo() == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping()
            || mob.isInvisible() || RenderFixer.isCarryOnRender(stack, bufferIn)) {
            return;
        }
        if (maid instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }

        if (stack.has(net.minecraft.core.component.DataComponents.TOOL)) {
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0.5, -0.25);
            if (maid instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                BackpackClientHelper.offsetBackpackItem(maid.getMaidBackpackType().getId(), matrixStack);
            } else {
                BackpackClientHelper.offsetBackpackItem(BackpackManager.getEmptyBackpack().getId(), matrixStack);
            }
            if (SlashBladeCompat.isSlashBladeItem(stack)) {
                SlashBladeRender.renderMaidBackSlashBlade(matrixStack, bufferIn, packedLightIn, stack);
            } else {
                ItemStackRenderState renderState = new ItemStackRenderState();
                Minecraft.getInstance().getItemModelResolver().updateForLiving(renderState, stack, ItemDisplayContext.FIXED, mob);
                renderState.submit(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 0);
            }
            matrixStack.popPose();
            return;
        }

        // 枪械额外渲染兼容
        GunClientUtil.renderBackGun(matrixStack, bufferIn, packedLightIn, stack, maid);
    }
}