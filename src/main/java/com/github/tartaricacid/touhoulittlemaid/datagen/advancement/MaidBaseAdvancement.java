package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.PickedUpItemTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Consumer;


public class MaidBaseAdvancement {
    public static void generate(Consumer<AdvancementHolder> saver) {
        AdvancementHolder root = make(Items.FEATHER, "switch_task")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_TASK))
                .save(saver, id("maid_base/switch_task"));

        generateTask(root, saver);

        generateOther(saver, existingFileHelper, root);

        generateBauble(root, saver);

        generatePhoto(root, saver);

        generateFind(saver, existingFileHelper, root);

        generateReborn(root, saver);
    }

    private static void generateFind(Consumer<AdvancementHolder> saver, AdvancementHolder root) {
        AdvancementHolder base = make(InitItems.SERVANT_BELL.get(), "use_servant_bell").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_SERVANT_BELL))
                .save(saver, id("maid_base/use_servant_bell"));

        make(InitItems.TRUMPET.get(), "use_trumpet").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_TRUMPET))
                .save(saver, id("maid_base/use_trumpet"));

        AdvancementHolder redFoxScroll = make(InitItems.RED_FOX_SCROLL.get(), "use_red_fox_scroll").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_RED_FOX_SCROLL))
                .save(saver, id("maid_base/use_red_fox_scroll"));

        make(InitItems.WHITE_FOX_SCROLL.get(), "use_white_fox_scroll").parent(redFoxScroll)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_WHITE_FOX_SCROLL))
                .save(saver, id("maid_base/use_white_fox_scroll"));
    }

    private static void generateOther(Consumer<AdvancementHolder> saver, AdvancementHolder root) {
        AdvancementHolder base = make(Items.SADDLE, "pickup_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PICKUP_MAID))
                .save(saver, id("maid_base/pickup_maid"));

        make(Items.EXPERIENCE_BOTTLE, "take_maid_xp").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAKE_MAID_XP))
                .save(saver, id("maid_base/take_maid_xp"));

        make(Items.MILK_BUCKET, "clear_maid_effects").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CLEAR_MAID_EFFECTS))
                .save(saver, id("maid_base/clear_maid_effects"));
    }

    private static void generateTask(AdvancementHolder root, Consumer<AdvancementHolder> saver) {
        AdvancementHolder taskRoot = make(Items.CLOCK, "switch_schedule").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_SCHEDULE))
                .save(saver, id("maid_base/switch_schedule"));

        AdvancementHolder backpack = make(InitItems.MAID_BACKPACK_BIG.get(), "maid_backpack").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_BACKPACK))
                .save(saver, id("maid_base/maid_backpack"));

        makeGoal(Items.DIAMOND_SWORD, "maid_kill_mob").parent(backpack)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_KILL_MOB))
                .save(saver, id("maid_base/maid_kill_mob"));

        make(Items.FISHING_ROD, "maid_fishing").parent(backpack)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FISHING))
                .save(saver, id("maid_base/maid_fishing"));

        AdvancementHolder farm = makeGoal(Items.IRON_HOE, "maid_farm").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FARM))
                .save(saver, id("maid_base/maid_farm"));

        AdvancementHolder feedAnimal = make(Items.WHEAT, "maid_feed_animal").parent(farm)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_ANIMAL))
                .save(saver, id("maid_base/maid_feed_animal"));

        make(Items.COOKED_BEEF, "maid_feed_player").parent(feedAnimal)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_PLAYER))
                .save(saver, id("maid_base/maid_feed_player"));
    }

    private static void generateBauble(AdvancementHolder root, Consumer<AdvancementHolder> saver) {
        AdvancementHolder baubleRoot = make(InitItems.FIRE_PROTECT_BAUBLE.get(), "use_protect_bauble").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_PROTECT_BAUBLE))
                .save(saver, id("maid_base/use_protect_bauble"));

        AdvancementHolder fabric = make(InitItems.NIMBLE_FABRIC.get(), "use_nimble_fabric").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_NIMBLE_FABRIC))
                .save(saver, id("maid_base/use_nimble_fabric"));

        makeGoal(InitItems.ULTRAMARINE_ORB_ELIXIR.get(), "use_undead_bauble").parent(fabric)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_UNDEAD_BAUBLE))
                .save(saver, id("maid_base/use_undead_bauble"));

        AdvancementHolder magnet = make(InitItems.ITEM_MAGNET_BAUBLE.get(), "use_item_magnet_bauble").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_ITEM_MAGNET_BAUBLE))
                .save(saver, id("maid_base/use_item_magnet_bauble"));

        make(InitItems.WIRELESS_IO.get(), "use_wireless_io").parent(magnet)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_WIRELESS_IO))
                .save(saver, id("maid_base/use_wireless_io"));
    }

    private static void generatePhoto(AdvancementHolder root, Consumer<AdvancementHolder> saver) {
        AdvancementHolder photoRoot = make(InitItems.CAMERA.get(), "photo_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PHOTO_MAID))
                .save(saver, id("maid_base/photo_maid"));

        AdvancementHolder statue = make(InitItems.CHISEL.get(), "chisel_statue").parent(photoRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHISEL_STATUE))
                .save(saver, id("maid_base/chisel_statue"));

        make(InitItems.GARAGE_KIT.get(), "pickup_garage_kit").parent(statue)
                .addCriterion("pickup_item", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(
                        Optional.empty(),
                        Optional.of(ItemPredicate.Builder.item().of(InitItems.GARAGE_KIT.get()).build()),
                        Optional.empty()))
                .save(saver, id("maid_base/pickup_garage_kit"));
    }

    private static void generateReborn(AdvancementHolder root, Consumer<AdvancementHolder> saver) {
        ItemStack stack = ItemEntityPlaceholder.setRecipeId(new ItemStack(InitItems.ENTITY_PLACEHOLDER.get()), "reborn_maid");
        AdvancementHolder rebornRoot = make(stack, "reborn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar_recipe/reborn_maid")))
                .save(saver, id("maid_base/reborn_maid"));

        makeGoal(InitItems.SHRINE.get(), "shrine_reborn_maid").parent(rebornRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SHRINE_REBORN_MAID))
                .save(saver, id("maid_base/shrine_reborn_maid"));
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.TASK, true, true, false);
    }

    private static Advancement.Builder make(ItemStack item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.GOAL, true, true, false);
    }

    private static Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, id);
    }
}
