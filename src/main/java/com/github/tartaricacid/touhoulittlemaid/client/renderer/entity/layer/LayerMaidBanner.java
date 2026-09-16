package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
// TODO: 26.2 中 Material 类被移除，旗帜渲染需要用 SpriteId 重写
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;


import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LayerMaidBanner extends RenderLayer<MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
    private final EntityMaidRenderer renderer;

    public LayerMaidBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
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
        if (stack.getItem() instanceof BannerItem bannerItem) {
            if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping() || mob.isInvisible()) {
                return;
            }
            if (maid instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
                return;
            }
            SimpleBedrockModel<LivingEntityRenderState> bannerModel = BedrockModelLoader.getModel(MAID_BANNER);
            if (bannerModel == null) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0, 0.5, 0.025);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.DummyVertexConsumer.INSTANCE;
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            BannerPatternLayers patterns = maid.getBackpackShowItem().get(DataComponents.BANNER_PATTERNS);
            DyeColor dyeColor = ((AbstractBannerBlock) bannerItem.getBlock()).getColor();
            if (patterns != null) {
                renderPatterns(matrixStack, bufferIn, packedLightIn, bannerModel.getPart("banner"), patterns, dyeColor);
            }
            matrixStack.popPose();
        }
    }

    private void renderPatterns(PoseStack poseStack, SubmitNodeCollector bufferSource, int packedLight,
                                BedrockPart banner, BannerPatternLayers patterns, DyeColor dyeColor) {
        // TODO: 26.2 中 Material 类被移除，需要用 SpriteId 和 SubmitNodeCollector 重写
        // 注释掉原有代码，等待后续重写
    }

    private void renderPatternLayer(PoseStack poseStack, SubmitNodeCollector buffer, int packedLight, BedrockPart banner, Object material, DyeColor color) {
        // TODO: 26.2 中 Material 类被移除，需要重写
    }
}
