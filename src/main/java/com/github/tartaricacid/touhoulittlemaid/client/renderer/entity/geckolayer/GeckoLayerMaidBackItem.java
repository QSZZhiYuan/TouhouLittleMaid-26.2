package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.GunClientUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.backpack.BackpackClientHelper;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


public class GeckoLayerMaidBackItem<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    public GeckoLayerMaidBackItem(R entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBackItem<>(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }
        var model = this.getLocationModel(entity);
        if (model == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!this.getGeoEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible() || RenderFixer.isCarryOnRender(stack, buffer)) {
            return;
        }
        if (entity instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }
        if (stack.has(net.minecraft.core.component.DataComponents.TOOL)) {
            matrixStack.pushPose();

            if (!model.backpackBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(matrixStack, model.backpackBones());
            }
            matrixStack.translate(0, 1, 0.25);

            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0.5, -0.25);
            if (entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                BackpackClientHelper.offsetBackpackItem(maid.getMaidBackpackType().getId(), matrixStack);
            } else {
                BackpackClientHelper.offsetBackpackItem(BackpackManager.getEmptyBackpack().getId(), matrixStack);
            }
            if (SlashBladeCompat.isSlashBladeItem(stack)) {
                SlashBladeRender.renderGeckoMaidBackSlashBlade(matrixStack, buffer, packedLight, stack);
            } else {
                ItemStackRenderState renderState = new ItemStackRenderState();
                Minecraft.getInstance().getItemModelResolver().updateForLiving(renderState, stack, ItemDisplayContext.FIXED, entity);
                renderState.submit(matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 0);
            }
            matrixStack.popPose();
            return;
        }

        // 背部枪械渲染
        GunClientUtil.renderBackGun(stack, model, maid, matrixStack, buffer, packedLight);
    }
}
