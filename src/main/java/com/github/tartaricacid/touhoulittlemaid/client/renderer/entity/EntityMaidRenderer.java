package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.ChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.EntityGraphics;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.state.MaidRenderState;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.SWarfareCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.patpat.PatPatCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("rawtypes,unchecked")
public class EntityMaidRenderer extends MobRenderer<Mob, MaidRenderState, BedrockModel<LivingEntityRenderState>> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    public static @Nullable Function<EntityRendererProvider.Context, IGeoEntityRenderer<Mob>> YSM_ENTITY_MAID_RENDERER;
    private final GeckoEntityMaidRenderer geckoEntityMaidRenderer;
    private final BedrockModel<LivingEntityRenderState> fallbackModel;
    private @Nullable IGeoEntityRenderer<Mob> ysmMaidRenderer;
    private ChatBubbleRenderer chatBubbleRenderer2;
    private MaidModelInfo mainInfo;
    private List<Object> mainAnimations = Lists.newArrayList();
    private boolean isGuiPreview = false;

    public EntityMaidRenderer(EntityRendererProvider.Context manager) {
        super(manager, new BedrockModel<>(), 0.5f);
        this.fallbackModel = this.model;
        this.addLayer((RenderLayer) new LayerMaidHeldItem(this, net.minecraft.client.Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()));
        this.addLayer((RenderLayer) new LayerMaidBipedHead(this, manager.getModelSet()));
        this.addLayer((RenderLayer) new LayerMaidBackpack(this, manager.getModelSet()));
        this.addLayer((RenderLayer) new LayerMaidBackItem(this));
        this.addLayer((RenderLayer) new LayerMaidBanner(this, manager.getModelSet()));
        this.addAdditionMaidLayer(manager);
        this.geckoEntityMaidRenderer = new GeckoEntityMaidRenderer<>(manager);
        this.initYsmModelRenderer(manager);
        this.chatBubbleRenderer2 = new ChatBubbleRenderer(this);
    }

    private void initYsmModelRenderer(EntityRendererProvider.Context manager) {
        if (!YsmCompat.isInstalled() || YSM_ENTITY_MAID_RENDERER == null) {
            return;
        }
        IGeoEntityRenderer<Mob> geoEntityRenderer = YSM_ENTITY_MAID_RENDERER.apply(manager);
        if (geoEntityRenderer != null) {
            this.ysmMaidRenderer = geoEntityRenderer;
            List<GeoLayerRenderer> layerRenderers = this.geckoEntityMaidRenderer.getLayerRenderers();
            for (GeoLayerRenderer layerRenderer : layerRenderers) {
                this.ysmMaidRenderer.addGeoLayerRenderer(layerRenderer.copy(this.ysmMaidRenderer));
            }
        }
    }

    @Override
    public MaidRenderState createRenderState() {
        return new MaidRenderState();
    }

    @Override
    public void extractRenderState(Mob entity, MaidRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.attackTime = entity.getAttackAnim(partialTicks);
        state.riding = entity.isPassenger();
        state.limbSwing = entity.walkAnimation.position(partialTicks);
        state.limbSwingAmount = entity.walkAnimation.speed(partialTicks);
        state.ageInTicks = entity.tickCount + partialTicks;
        state.netHeadYaw = state.yRot;
        state.headPitch = state.xRot;
        state.partialTick = partialTicks;

        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }

        this.model = CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID).orElse(this.fallbackModel);
        this.mainInfo = CustomPackLoader.MAID_MODELS.getInfo(DEFAULT_MODEL_ID).orElse(null);
        this.mainAnimations = CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).orElse(null);

        MaidModels.ModelData eventModelData = new MaidModels.ModelData(model, mainInfo, mainAnimations);
        if (NeoForge.EVENT_BUS.post(new RenderMaidEvent(maid, eventModelData)).isCanceled()) {
            BedrockModel<LivingEntityRenderState> bedrockModel = eventModelData.getModel();
            if (bedrockModel != null) {
                this.model = bedrockModel;
            }
            this.mainInfo = eventModelData.getInfo();
            this.mainAnimations = eventModelData.getAnimations();
        } else {
            CustomPackLoader.MAID_MODELS.getModel(maid.getModelId()).ifPresent(model -> this.model = model);
            CustomPackLoader.MAID_MODELS.getInfo(maid.getModelId()).ifPresent(info -> this.mainInfo = info);
            this.mainAnimations = CustomPackLoader.MAID_MODELS.getAnimation(maid.getModelId()).orElse(this.mainAnimations);
        }

        state.info = this.mainInfo;
        state.animations = this.mainAnimations;
        state.modelId = maid.getModelId();
        state.model = this.model;
    }

    @Override
    public void submit(MaidRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        if (state.entity == null) {
            return;
        }
        this.model = state.model;
        this.mainInfo = state.info;
        this.mainAnimations = state.animations;
        Mob entity = (Mob) state.entity;
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }

        if (SWarfareCompat.shouldHideLivingRender(entity)) {
            return;
        }

        float partialTicks = state.partialTick;
        float entityYaw = state.yRot;
        int packedLightIn = state.lightCoords;

        EntityMaid maidEntity = maid.asStrictMaid();
        if (maidEntity != null && MaidConfig.GLOBAL_MAID_SHOW_CHAT_BUBBLE.get() && maidEntity.getConfigManager().isChatBubbleShow()) {
            Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
            if (vec3 != null) {
                poseStack.pushPose();
                double offsetY = vec3.y() + 0.5f;
                if (maidEntity.isMaidInSittingPose()) {
                    offsetY -= 0.25f;
                }
                poseStack.translate(vec3.x, offsetY, vec3.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                poseStack.scale(-0.025F, -0.025F, 0.025F);
                EntityGraphics graphics = new EntityGraphics(bufferIn, poseStack, maidEntity, packedLightIn, partialTicks);
                this.chatBubbleRenderer2.render(graphics);
                poseStack.popPose();
            }
        }

        if (maid.isYsmModel() && this.ysmMaidRenderer != null) {

            IGeoEntity geoEntity = this.ysmMaidRenderer.getGeoEntity(entity);
            geoEntity.setYsmModel(maid.getYsmModelId(), maid.getYsmModelTexture());
            if (maidEntity != null) {
                geoEntity.updateRoamingVars(maidEntity.roamingVars);
            }
            PatPatCompat.renderPat(entity, poseStack, partialTicks);
            this.ysmMaidRenderer.geoRender(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
            return;
        }

        if (this.mainInfo != null && this.mainInfo.isGeckoModel()) {

            this.geckoEntityMaidRenderer.getAnimatableEntity(entity).setMaidInfo(this.mainInfo);
            PatPatCompat.renderPat(entity, poseStack, partialTicks);
            if (!camera.initialized) {
                float savedYBodyRot = entity.yBodyRot;
                float savedYBodyRotO = entity.yBodyRotO;
                entity.yBodyRot = state.yRot;
                entity.yBodyRotO = state.yRot;
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                this.geckoEntityMaidRenderer.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
                poseStack.popPose();
                entity.yBodyRot = savedYBodyRot;
                entity.yBodyRotO = savedYBodyRotO;
            } else {
                this.geckoEntityMaidRenderer.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
            }
            return;
        }

        this.model.setAnimations(this.mainAnimations);
        this.isGuiPreview = !camera.initialized;
        GlWrapper.withPoseStack(poseStack, () -> super.submit(state, poseStack, bufferIn, camera));

    }

    @Override
    protected void scale(MaidRenderState state, PoseStack poseStack) {
        if (mainInfo != null) {
            float scale = mainInfo.getRenderEntityScale();
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    protected void setupRotations(MaidRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);

        if (state.entity != null && state.entity.getVehicle() instanceof Player && this.mainInfo != null && !this.mainInfo.isGeckoModel()) {
            poseStack.translate(-0.375, 0.8325, 0.375);
            poseStack.mulPose(Axis.ZN.rotationDegrees(65));
            poseStack.mulPose(Axis.YN.rotationDegrees(-80));
        }

        if (state.entity instanceof Mob mob && this.mainInfo != null) {
            HardcodedAnimationManger.setupRotations(mob, poseStack, state.ageInTicks, bodyRot, state.partialTick, this.mainInfo.isGeckoModel());
        }
    }

    @Override
    public Identifier getTextureLocation(MaidRenderState state) {
        if (mainInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return mainInfo.getTexture();
    }

    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }

    public EntityRenderDispatcher getDispatcher() {
        return this.entityRenderDispatcher;
    }

    private void addAdditionMaidLayer(EntityRendererProvider.Context renderManager) {
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addAdditionMaidLayer(this, renderManager);
        }
    }
}
