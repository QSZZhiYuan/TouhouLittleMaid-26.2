package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.ReplaceableResolvedModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT)
public final class InitSpecialItemRender {
    private static final List<Triple<Identifier, Identifier, Supplier<Boolean>>> REPLACEABLE_MODEL_LIST = Lists.newArrayList();

    private static final Identifier LIFE_POINT = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "life_point");
    private static final Identifier POINT_ITEM = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "point_item");

    private static final Identifier SPAWN_BOX = standalone("item/spawn_box");
    private static final Identifier REBORN_MAID = standalone("item/reborn_maid");
    private static final Identifier SPAWN_LIGHTNING_BOLT = standalone("item/spawn_lightning_bolt");

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addReplaceableModel(Items.TOTEM_OF_UNDYING, LIFE_POINT, () -> VanillaConfig.REPLACE_TOTEM_TEXTURE.get());
            addReplaceableModel(Items.EXPERIENCE_BOTTLE, POINT_ITEM, () -> VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE.get());
        }
    }

    @SubscribeEvent
    public static void onResolvedModel(ModelEvent.BakingCompleted event) {
        // 26.2: getBakedTopLevelModels() 已移除，resolvedModels 为私有字段，此功能需要重新实现
        // Map<Identifier, ResolvedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        // for (Triple<Identifier, Identifier, Supplier<Boolean>> triple : REPLACEABLE_MODEL_LIST) {
        //     ReplaceableResolvedModel model = new ReplaceableResolvedModel(registry.get(triple.getLeft()), registry.get(triple.getMiddle()), triple.getRight());
        //     registry.put(triple.getLeft(), model);
        // }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterStandalone event) {
        // 26.2: RegisterStandalone.register 签名变更为 register(StandaloneModelKey<T>, UnbakedStandaloneModel<T>)
        // REPLACEABLE_MODEL_LIST.forEach((triple) -> event.register(triple.getMiddle()));
        // event.register(SPAWN_BOX);
        // event.register(REBORN_MAID);
        // event.register(SPAWN_LIGHTNING_BOLT);
    }

    public static void addReplaceableModel(Item item, Identifier replacedModel, Supplier<Boolean> isReplace) {
        Identifier res = BuiltInRegistries.ITEM.getKey(item);
        if (res != null) {
            Identifier rawModelResourceLocation = res;
            Identifier replacedModelResourceLocation = Identifier.fromNamespaceAndPath(replacedModel.getNamespace(), "item/" + replacedModel.getPath());
            REPLACEABLE_MODEL_LIST.add(Triple.of(rawModelResourceLocation, replacedModelResourceLocation, isReplace));
        }
    }

    private static Identifier standalone(String path) {
        return Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, path);
    }
}
