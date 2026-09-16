package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.concurrent.CompletableFuture;

public class TagEntity extends EntityTypeTagsProvider {
    /**
     * 女仆妖精的攻击目标，默认仅攻击铁傀儡和玩家
     */
    public static TagKey<EntityType<?>> MAID_FAIRY_ATTACK_GOAL = createTagKey("maid_fairy_attack_goal");

    /**
     * 女仆在骑乘时，为了朝向一致，会强制同步女仆朝向和当前骑乘实体朝向；
     * <p>
     * 但是部分模组（如机械动力）这么做反而会导致女仆异常旋转，故添加此标签
     */
    public static TagKey<EntityType<?>> MAID_VEHICLE_ROTATE_BLOCKLIST = createTagKey("maid_vehicle_rotate_blocklist");

    public static TagKey<EntityType<?>> CARRYON_ENTITY_BLACKLIST = createTagKey(Identifier.parse("carryon:entity_blacklist"));

    /**
     * 冰与火的石化效果免疫标签
     */
    public static final TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = createTagKey(
            Identifier.parse("iceandfire:immune_to_gorgon_stone")
    );


    public TagEntity(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, TouhouLittleMaid.MOD_ID);
    }

    private static TagKey<EntityType<?>> createTagKey(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, name));
    }

    private static TagKey<EntityType<?>> createTagKey(Identifier id) {
        return TagKey.create(Registries.ENTITY_TYPE, id);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.DANMAKU.get()).orElseThrow());
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.FAIRY.get()).orElseThrow());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.FAIRY.get()).orElseThrow());
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.FAIRY.get()).orElseThrow());

        tag(MAID_FAIRY_ATTACK_GOAL).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityTypes.IRON_GOLEM).orElseThrow())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("guardvillagers:guard")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("earthtojavamobs:furnace_golem")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("earthmobsmod:furnace_golem")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("mutantmonsters:mutant_snow_golem")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("alexscaves:gingerbread_man")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("alexsmobs:bunfungus")));

        tag(MAID_VEHICLE_ROTATE_BLOCKLIST)
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("create:carriage_contraption")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, id("create:seat")));

        tag(CARRYON_ENTITY_BLACKLIST).add(
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.TOMBSTONE.get()).orElseThrow(),
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.SIT.get()).orElseThrow(),
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.BROOM.get()).orElseThrow());

        // 让女仆免疫冰与火的石化效果，避免石化带来的各种问题
        tag(IMMUNE_TO_GORGON_STONE).add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(InitEntities.MAID.get()).orElseThrow());
    }

    private Identifier id(String name) {
        return Identifier.parse(name);
    }
}
