package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;


import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
    private final EntityModelSet modelSet;
    private final ModelPart flag;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.flag = modelSet.bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBanner<>(entityRendererIn, modelSet);
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null || !(maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        if (!getGeoEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
            return;
        }
        if (!maid.getConfigManager().isShowBackItem()) {
            return;
        }
        ILocationModel geoModel = getLocationModel(entity);
        if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
            SimpleBedrockModel<EntityRenderState> bannerModel = BedrockModelLoader.getModel(MAID_BANNER);
            if (bannerModel == null) {
                return;
            }
            poseStack.pushPose();
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.backpackBones());
            poseStack.translate(0, 0.75, 0.3);
            poseStack.scale(0.65F, -0.65F, -0.65F);
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(5));
            RenderHelper.renderModelToBuffer(bannerModel, poseStack, bufferIn, RenderTypes.entitySolid(TEXTURE), packedLight, OverlayTexture.NO_OVERLAY);
            BannerPatternLayers patterns = maid.getBackpackShowItem().get(DataComponents.BANNER_PATTERNS);
            DyeColor dyeColor = ((AbstractBannerBlock) bannerItem.getBlock()).getColor();
            if (patterns != null) {
                renderPatterns(poseStack, bufferIn, packedLight, bannerModel.getPart("banner"), patterns, dyeColor);
            }
            poseStack.popPose();
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
