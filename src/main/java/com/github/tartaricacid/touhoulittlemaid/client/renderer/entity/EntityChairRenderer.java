package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChairRenderer extends LivingEntityRenderer<EntityChair, EntityChairRenderer.ChairRenderState, BedrockModel<LivingEntityRenderState>> {
    public static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_CHAIR_ID = "touhou_little_maid:cushion";
    public static boolean renderHitBox = true;
    private ChairModelInfo chairInfo;
    private List<Object> chairAnimations;
    private final GeckoEntityChairRenderer geckoEntityChairRenderer;

    public static class ChairRenderState extends LivingEntityRenderState {
        public EntityChair entity;
        public AABB hitBox;
        public boolean isGeckoModel;
        public BedrockModel<LivingEntityRenderState> model;
        public ChairModelInfo chairInfo;
        public List<Object> chairAnimations;
    }

    public EntityChairRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new BedrockModel<>(), 0);
        this.geckoEntityChairRenderer = new GeckoEntityChairRenderer(rendererManager);
    }

    @Override
    public ChairRenderState createRenderState() {
        return new ChairRenderState();
    }

    @Override
    public void extractRenderState(EntityChair chair, ChairRenderState state, float partialTicks) {
        super.extractRenderState(chair, state, partialTicks);
        state.entity = chair;
        state.hitBox = chair.getBoundingBox().move(-chair.getX(), -chair.getY(), -chair.getZ());

        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.CHAIR_MODELS.getModel(DEFAULT_CHAIR_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODELS.getInfo(DEFAULT_CHAIR_ID).ifPresent(info -> this.chairInfo = info);
        this.chairAnimations = null;

        // 通过模型 id 获取对应数据
        CustomPackLoader.CHAIR_MODELS.getModel(chair.getModelId()).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODELS.getInfo(chair.getModelId()).ifPresent(info -> this.chairInfo = info);
        CustomPackLoader.CHAIR_MODELS.getAnimation(chair.getModelId()).ifPresent(animations -> this.chairAnimations = animations);

        state.isGeckoModel = this.chairInfo != null && this.chairInfo.isGeckoModel();
        state.model = this.model;
        state.chairInfo = this.chairInfo;
        state.chairAnimations = this.chairAnimations;
    }

    @Override
    public void submit(ChairRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.model;
        this.chairInfo = state.chairInfo;
        this.chairAnimations = state.chairAnimations;
        LocalPlayer player = Minecraft.getInstance().player;
        if (canShowHitBox(player) && renderHitBox) {
            renderHitBox(state, poseStack, submitNodeCollector);
        } else {
            renderChair(state, poseStack, submitNodeCollector, camera);
        }
    }

    private boolean canShowHitBox(@Nullable Player player) {
        if (player != null && player.isShiftKeyDown()) {
            return player.getMainHandItem().getItem() == InitItems.CHAIR_SHOW.get();
        }
        return false;
    }

    private void renderHitBox(ChairRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        VoxelShape shape = Shapes.create(state.hitBox);
        submitNodeCollector.submitShapeOutline(poseStack, shape, RenderTypes.lines(), 0xFFFF0000, 1.0F, false);
    }

    private void renderChair(ChairRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        // GeckoLib 接管渲染
        if (state.isGeckoModel) {
            this.geckoEntityChairRenderer.setMainInfo(this.chairInfo);
            this.geckoEntityChairRenderer.getAnimatableEntity(state.entity).setChair(this.chairInfo);
            this.geckoEntityChairRenderer.render(state.entity, state.bodyRot, state.partialTick, poseStack, submitNodeCollector, state.lightCoords);
            return;
        }

        // 模型动画设置
        this.model.setAnimations(this.chairAnimations);

        GlWrapper.setPoseStack(poseStack);
        super.submit(state, poseStack, submitNodeCollector, camera);

    }

    @Override
    protected void scale(ChairRenderState state, PoseStack poseStack) {
        float scale = chairInfo.getRenderEntityScale();
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTextureLocation(ChairRenderState state) {
        if (chairInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return chairInfo.getTexture();
    }

    @Override
    protected void setupRotations(ChairRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - bodyRot));
    }

    @Override
    protected boolean shouldShowName(EntityChair entity, double distanceToCameraSq) {
        return entity.shouldShowName();
    }
}
