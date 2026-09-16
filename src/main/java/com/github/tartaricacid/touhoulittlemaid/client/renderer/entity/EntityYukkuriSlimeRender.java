package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.cubemob.Slime;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.REIMU_YUKKURI;

public class EntityYukkuriSlimeRender extends MobRenderer<Slime, SlimeRenderState, SimpleBedrockModel<SlimeRenderState>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/reimu_yukkuri.png");
    private final SlimeRenderer vanillaRender;

    public EntityYukkuriSlimeRender(EntityRendererProvider.Context context) {
        super(context, new SimpleBedrockModel<>(), 0.25F);
        this.vanillaRender = new SlimeRenderer(context);
    }

    @Override
    public SlimeRenderState createRenderState() {
        return new SlimeRenderState();
    }

    @Override
    public void extractRenderState(Slime entity, SlimeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish);
        state.size = entity.getSize();
    }

    @Override
    public void submit(SlimeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (VanillaConfig.REPLACE_SLIME_MODEL.get()) {
            SimpleBedrockModel<SlimeRenderState> model = BedrockModelLoader.getModel(REIMU_YUKKURI);
            if (model != null) {
                this.model = model;
            }
            this.shadowRadius = 0.25F * state.size;
            super.submit(state, poseStack, submitNodeCollector, camera);
        } else {
            vanillaRender.submit(state, poseStack, submitNodeCollector, camera);
        }
    }

    @Override
    protected void scale(SlimeRenderState state, PoseStack poseStack) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float slimeSize = state.size;
        float tmp = state.squish / (slimeSize * 0.5F + 1.0F);
        float scale = 1.0F / (tmp + 1.0F);
        poseStack.scale(scale * slimeSize, 1.0F / scale * slimeSize, scale * slimeSize);
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return TEXTURE;
    }
}
