package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.*;

public class BackpackClientHelper {
    private static final Identifier EMPTY_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "empty");
    private static final Identifier SMALL_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "small_backpack");
    private static final Identifier MIDDLE_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "middle_backpack");
    private static final Identifier BIG_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "big_backpack");
    private static final Identifier CRAFTING_TABLE_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "crafting_table_backpack");
    private static final Identifier ENDER_CHEST_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "ender_chest_backpack");
    private static final Identifier FURNACE_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "furnace_backpack");
    private static final Identifier TANK_ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "tank");

    private static Map<Identifier, Pair<EntityModel<?>, Identifier>> BACKPACK_MODEL_MAP;

    public static void init(EntityModelSet modelSet) {
        BACKPACK_MODEL_MAP = new java.util.HashMap<>();
        put(SMALL_ID, BedrockModelLoader.getModel(SMALL_BACKPACK), "textures/bedrock/entity/backpack/small_backpack.png");
        put(MIDDLE_ID, BedrockModelLoader.getModel(MIDDLE_BACKPACK), "textures/bedrock/entity/backpack/middle_backpack.png");
        put(BIG_ID, BedrockModelLoader.getModel(BIG_BACKPACK), "textures/bedrock/entity/backpack/big_backpack.png");
        put(CRAFTING_TABLE_ID, BedrockModelLoader.getModel(CRAFTING_TABLE_BACKPACK), "textures/bedrock/entity/backpack/crafting_table_backpack.png");
        put(ENDER_CHEST_ID, BedrockModelLoader.getModel(END_CHEST_BACKPACK), "textures/bedrock/entity/backpack/ender_chest_backpack.png");
        put(FURNACE_ID, BedrockModelLoader.getModel(FURNACE_BACKPACK), "textures/bedrock/entity/backpack/furnace_backpack.png");
        put(TANK_ID, BedrockModelLoader.getModel(TANK_BACKPACK), "textures/bedrock/entity/backpack/tank_backpack.png");
        BACKPACK_MODEL_MAP = java.util.Collections.unmodifiableMap(BACKPACK_MODEL_MAP);
    }

    private static void put(Identifier id, EntityModel<?> model, String texturePath) {
        BACKPACK_MODEL_MAP.put(id, Pair.of(model, Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, texturePath)));
    }

    @Nullable
    public static EntityModel<?> getBackpackModel(Identifier id) {
        Pair<EntityModel<?>, Identifier> pair = BACKPACK_MODEL_MAP == null ? null : BACKPACK_MODEL_MAP.get(id);
        return pair == null ? null : pair.getLeft();
    }

    @Nullable
    public static Identifier getBackpackTexture(Identifier id) {
        Pair<EntityModel<?>, Identifier> pair = BACKPACK_MODEL_MAP == null ? null : BACKPACK_MODEL_MAP.get(id);
        return pair == null ? null : pair.getRight();
    }

    public static Optional<Pair<EntityModel<?>, Identifier>> findBackpackModel(Identifier id) {
        Pair<EntityModel<?>, Identifier> pair = BACKPACK_MODEL_MAP == null ? null : BACKPACK_MODEL_MAP.get(id);
        if (pair == null || pair.getLeft() == null) {
            return Optional.empty();
        }
        return Optional.of(pair);
    }

    public static void offsetBackpackItem(Identifier id, PoseStack poseStack) {
        Consumer<PoseStack> offset = getOffset(id);
        if (offset != null) {
            offset.accept(poseStack);
        }
    }

    @Nullable
    private static Consumer<PoseStack> getOffset(Identifier id) {
        if (id.equals(EMPTY_ID)) {
            return ps -> ps.translate(0, 0.625, 0.2);
        }
        if (id.equals(SMALL_ID)) {
            return ps -> ps.translate(0, 0.625, -0.05);
        }
        if (id.equals(MIDDLE_ID)) {
            return ps -> { ps.mulPose(Axis.XP.rotationDegrees(-7.5F)); ps.translate(0, 0.625, -0.25); };
        }
        if (id.equals(BIG_ID)) {
            return ps -> ps.translate(0, 0.25, -0.25);
        }
        if (id.equals(CRAFTING_TABLE_ID)) {
            return ps -> ps.translate(0, 0.625, 0.25);
        }
        if (id.equals(ENDER_CHEST_ID)) {
            return ps -> ps.translate(0, 0.625, 0.25);
        }
        if (id.equals(FURNACE_ID)) {
            return ps -> ps.translate(0, 0.625, 0.25);
        }
        if (id.equals(TANK_ID)) {
            return ps -> { ps.mulPose(Axis.XP.rotationDegrees(-7.5F)); ps.translate(0, 0.625, -0.25); };
        }
        return null;
    }
}