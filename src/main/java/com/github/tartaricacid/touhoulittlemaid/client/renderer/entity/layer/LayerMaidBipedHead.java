package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SimpleHatsCompat;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LayerMaidBipedHead extends RenderLayer<MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final EntityMaidRenderer maidRenderer;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;
    private final BlockModelResolver blockModelResolver;
    private final BlockModelRenderState blockModelRenderState = new BlockModelRenderState();

    public LayerMaidBipedHead(EntityMaidRenderer maidRenderer, EntityModelSet modelSet) {
        super(maidRenderer);
        this.maidRenderer = maidRenderer;
        this.skullModels = new HashMap<>();
        for (SkullBlock.Types type : SkullBlock.Types.values()) {
            SkullModelBase model = SkullBlockRenderer.createModel(modelSet, type);
            if (model != null) {
                this.skullModels.put(type, model);
            }
        }
        this.blockModelResolver = Minecraft.getInstance().getBlockModelResolver();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn, MaidRenderState state, float yRot, float xRot) {
        if (!(state.entity instanceof Mob mob)) return;
        if (maidRenderer.getMainInfo() == null) return;
        boolean allowRenderHead = maidRenderer.getMainInfo().isShowCustomHead() && getParentModel().hasHead();
        if (!allowRenderHead) {
            return;
        }

        // 渲染头盔栏的
        ItemStack head = mob.getItemBySlot(EquipmentSlot.HEAD);
        if (!head.isEmpty()) {
            Item item = head.getItem();
            poseStack.pushPose();
            this.getParentModel().getHead().translateAndRotate(poseStack);
            if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof net.minecraft.world.level.block.AbstractSkullBlock skullBlock) {
                poseStack.scale(1.1875F, -1.1875F, -1.1875F);
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                SkullBlock.Type type = skullBlock.getType();
                SkullModelBase modelBase = this.skullModels.get(type);
                RenderType rendertype = SkullBlockRenderer.getSkullRenderType(type, null);
                SkullBlockRenderer.submitSkull(0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype, 0, null);
            }
            poseStack.popPose();
        }

        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }

        // 渲染女仆背部的
        ItemStack stack = maid.getBackpackShowItem();
        // 不做限制，任意方块都可以显示
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState blockState = blockItem.getBlock().defaultBlockState();
            poseStack.pushPose();
            this.getParentModel().getHead().translateAndRotate(poseStack);
            poseStack.scale(0.8F, -0.8F, -0.8F);
            poseStack.translate(-0.5, 0.625, -0.5);
            this.blockModelResolver.update(this.blockModelRenderState, blockState, BLOCK_DISPLAY_CONTEXT);
            this.blockModelRenderState.submit(poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        } else {
            SimpleHatsCompat.renderHat(poseStack, bufferIn, packedLightIn, mob, stack, this.getParentModel());
        }
    }
}
