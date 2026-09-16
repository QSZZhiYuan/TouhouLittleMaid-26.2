package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SimpleHatsCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

import java.util.HashMap;
import java.util.Map;

public class GeckoLayerMaidBipedHead<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;
    private final EntityModelSet modelSet;

    public GeckoLayerMaidBipedHead(R entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
        this.modelSet = modelSet;
        this.skullModels = new HashMap<>();
        for (SkullBlock.Type type : new SkullBlock.Type[]{SkullBlock.Types.SKELETON, SkullBlock.Types.WITHER_SKELETON, SkullBlock.Types.PLAYER, SkullBlock.Types.ZOMBIE, SkullBlock.Types.CREEPER, SkullBlock.Types.DRAGON, SkullBlock.Types.PIGLIN}) {
            SkullModelBase model = SkullBlockRenderer.createModel(modelSet, type);
            if (model != null) {
                skullModels.put(type, model);
            }
        }
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBipedHead<>(entityRendererIn, modelSet);
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var animatableEntity = getGeoEntity(entity);
        if (animatableEntity.getGeoModel() != null) {
            ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
            ILocationModel model = animatableEntity.getGeoModel();
            boolean allowRenderHead = animatableEntity.getMaidInfo().isShowCustomHead() && !model.headBones().isEmpty();
            if (!allowRenderHead) {
                return;
            }

            if (!head.isEmpty()) {
                Item item = head.getItem();
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, model.headBones());
                if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock skullBlock) {
                    poseStack.scale(-1.1875F, 1.1875F, -1.1875F);
                    poseStack.translate(-0.5D, 0.0D, -0.5D);
                    SkullBlock.Type type = skullBlock.getType();
                    SkullModelBase modelBase = this.skullModels.get(type);
                    RenderType rendertype = SkullBlockRenderer.getSkullRenderType(type, null);
                    SkullBlockRenderer.submitSkull(0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype, 0, null);
                }
                poseStack.popPose();
            }

            IMaid maid = IMaid.convert(entity);
            if (maid == null) {
                return;
            }

            ItemStack stack = maid.getBackpackShowItem();
            if (stack.getItem() instanceof BlockItem blockItem) {
                // TODO: 26.2 中 Minecraft.getBlockRenderer() 已移除，方块渲染需要用新 API 重写
            } else {
                SimpleHatsCompat.renderGeckoHat(poseStack, bufferIn, packedLightIn, entity, stack, model.headBones());
            }
        }
    }
}
