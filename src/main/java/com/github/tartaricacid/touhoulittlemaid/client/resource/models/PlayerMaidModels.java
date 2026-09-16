package com.github.tartaricacid.touhoulittlemaid.client.resource.models;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.model.PlayerMaidModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@OnlyIn(Dist.CLIENT)
public final class PlayerMaidModels {
    private static final PlayerMaidModel PLAYER_MAID_MODEL = new PlayerMaidModel(false);
    private static final PlayerMaidModel PLAYER_MAID_MODEL_SLIM = new PlayerMaidModel(true);
    private static final List<Identifier> PLAYER_MAID_ANIMATION_RES = Lists.newArrayList(
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/default.js"),
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/beg.js"),
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/default/leg/default.js"),
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/player/arm/default.js"),
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/default/arm/swing.js"),
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "animation/maid/player/sit/default.js")
    );
    private static final Identifier TEXTURE_ALEX = Identifier.withDefaultNamespace("textures/entity/alex.png");
    private static final List<Object> PLAYER_MAID_ANIMATIONS = Lists.newArrayList();
    private static MaidModelInfo playerMaidInfo;
    private static Identifier playerSkin;

    public static void reload() {
        PLAYER_MAID_ANIMATIONS.clear();
        for (Identifier res : PLAYER_MAID_ANIMATION_RES) {
            PLAYER_MAID_ANIMATIONS.add(InnerAnimation.get(res));
        }
        playerMaidInfo = new MaidModelInfo() {
            @Override
            public Identifier getTexture() {
                return playerSkin;
            }
        };
    }

    public static BedrockModel<LivingEntityRenderState> getPlayerMaidModel(String name) {
        return PLAYER_MAID_MODEL;
    }

    public static List<Object> getPlayerMaidAnimations() {
        return PLAYER_MAID_ANIMATIONS;
    }

    public static MaidModelInfo getPlayerMaidInfo(String name) {
        playerSkin = getPlayerSkin(name);
        return playerMaidInfo;
    }

    public static Identifier getPlayerSkin(String name) {
        return TEXTURE_ALEX;
    }
}
