package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TouhouLittleMaid.MOD_ID);

    public static DeferredItem<Item> MAID_BACKPACK_SMALL = ITEMS.registerItem("maid_backpack_small", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> MAID_BACKPACK_MIDDLE = ITEMS.registerItem("maid_backpack_middle", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> MAID_BACKPACK_BIG = ITEMS.registerItem("maid_backpack_big", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> CRAFTING_TABLE_BACKPACK = ITEMS.registerItem("crafting_table_backpack", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> ENDER_CHEST_BACKPACK = ITEMS.registerItem("ender_chest_backpack", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> FURNACE_BACKPACK = ITEMS.registerItem("furnace_backpack", ItemMaidBackpack::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> TANK_BACKPACK = ITEMS.registerItem("tank_backpack", ItemTankBackpack::new);
    public static DeferredItem<Item> CHAIR = ITEMS.registerItem("chair", ItemChair::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> HAKUREI_GOHEI = ITEMS.registerItem("hakurei_gohei", ItemHakureiGohei::new);
    public static DeferredItem<Item> SANAE_GOHEI = ITEMS.registerItem("sanae_gohei", ItemHakureiGohei::new);
    public static DeferredItem<Item> MAID_BED = ITEMS.registerItem("maid_bed", ItemMaidBed::new);
    public static DeferredItem<Item> EXTINGUISHER = ITEMS.registerItem("extinguisher", ItemExtinguisher::new, p -> p.durability(128));
    public static DeferredItem<Item> ULTRAMARINE_ORB_ELIXIR = ITEMS.registerItem("ultramarine_orb_elixir", p -> new ItemDamageableBauble(6, p));
    public static DeferredItem<Item> EXPLOSION_PROTECT_BAUBLE = ITEMS.registerItem("explosion_protect_bauble", p -> new ItemDamageableBauble(32, p));
    public static DeferredItem<Item> FIRE_PROTECT_BAUBLE = ITEMS.registerItem("fire_protect_bauble", p -> new ItemDamageableBauble(128, p));
    public static DeferredItem<Item> PROJECTILE_PROTECT_BAUBLE = ITEMS.registerItem("projectile_protect_bauble", p -> new ItemDamageableBauble(64, p));
    public static DeferredItem<Item> MAGIC_PROTECT_BAUBLE = ITEMS.registerItem("magic_protect_bauble", p -> new ItemDamageableBauble(128, p));
    public static DeferredItem<Item> FALL_PROTECT_BAUBLE = ITEMS.registerItem("fall_protect_bauble", p -> new ItemDamageableBauble(32, p));
    public static DeferredItem<Item> DROWN_PROTECT_BAUBLE = ITEMS.registerItem("drown_protect_bauble", p -> new ItemDamageableBauble(64, p));
    public static DeferredItem<Item> NIMBLE_FABRIC = ITEMS.registerItem("nimble_fabric", p -> new ItemDamageableBauble(64, p));
    public static DeferredItem<Item> ITEM_MAGNET_BAUBLE = ITEMS.registerItem("item_magnet_bauble", ItemNormalBauble::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> MUTE_BAUBLE = ITEMS.registerItem("mute_bauble", ItemNormalBauble::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> ENTITY_PLACEHOLDER = ITEMS.registerItem("entity_placeholder", ItemEntityPlaceholder::new);
    public static DeferredItem<Item> SUBSTITUTE_JIZO = ITEMS.registerItem("substitute_jizo", ItemSubstituteJizo::new, p -> p.stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> POWER_POINT = ITEMS.registerItem("power_point", ItemPowerPoint::new);
    public static DeferredItem<Item> CAMERA = ITEMS.registerItem("camera", ItemCamera::new, p -> p.stacksTo(1).durability(50));
    public static DeferredItem<Item> PHOTO = ITEMS.registerItem("photo", ItemPhoto::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> FILM = ITEMS.registerItem("film", ItemFilm::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> CHISEL = ITEMS.registerItem("chisel", ItemChisel::new, p -> p.stacksTo(1).durability(64));
    public static DeferredItem<Item> GARAGE_KIT = ITEMS.registerItem("garage_kit", ItemGarageKit::new);
    public static DeferredItem<Item> SMART_SLAB_INIT = ITEMS.registerItem("smart_slab_init", p -> new ItemSmartSlab(ItemSmartSlab.Type.INIT, p));
    public static DeferredItem<Item> SMART_SLAB_EMPTY = ITEMS.registerItem("smart_slab_empty", p -> new ItemSmartSlab(ItemSmartSlab.Type.EMPTY, p));
    public static DeferredItem<Item> SMART_SLAB_HAS_MAID = ITEMS.registerItem("smart_slab_has_maid", p -> new ItemSmartSlab(ItemSmartSlab.Type.HAS_MAID, p));
    public static DeferredItem<Item> TRUMPET = ITEMS.registerItem("trumpet", ItemTrumpet::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> WIRELESS_IO = ITEMS.registerItem("wireless_io", ItemWirelessIO::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> MAID_BEACON = ITEMS.registerItem("maid_beacon", ItemMaidBeacon::new);
    public static DeferredItem<Item> MODEL_SWITCHER = ITEMS.registerItem("model_switcher", ItemModelSwitcher::new);
    public static DeferredItem<Item> CHAIR_SHOW = ITEMS.registerItem("chair_show", ItemChairShow::new);
    public static DeferredItem<Item> GOMOKU = ITEMS.registerItem("gomoku", p -> new BlockItem(InitBlocks.GOMOKU.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> CCHESS = ITEMS.registerItem("cchess", p -> new BlockItem(InitBlocks.CCHESS.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> WCHESS = ITEMS.registerItem("wchess", p -> new BlockItem(InitBlocks.WCHESS.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> RED_FOX_SCROLL = ITEMS.registerItem("red_fox_scroll", ItemFoxScroll::new);
    public static DeferredItem<Item> WHITE_FOX_SCROLL = ITEMS.registerItem("white_fox_scroll", ItemFoxScroll::new);
    public static DeferredItem<Item> KEYBOARD = ITEMS.registerItem("keyboard", p -> new BlockItem(InitBlocks.KEYBOARD.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> BOOKSHELF = ITEMS.registerItem("bookshelf", p -> new BlockItem(InitBlocks.BOOKSHELF.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> COMPUTER = ITEMS.registerItem("computer", p -> new BlockItem(InitBlocks.COMPUTER.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> FAVORABILITY_TOOL_ADD = ITEMS.registerItem("favorability_tool_add", p -> new ItemFavorabilityTool("add", p));
    public static DeferredItem<Item> FAVORABILITY_TOOL_REDUCE = ITEMS.registerItem("favorability_tool_reduce", p -> new ItemFavorabilityTool("reduce", p));
    public static DeferredItem<Item> FAVORABILITY_TOOL_FULL = ITEMS.registerItem("favorability_tool_full", p -> new ItemFavorabilityTool("full", p));
    public static DeferredItem<Item> SHRINE = ITEMS.registerItem("shrine", p -> new BlockItem(InitBlocks.SHRINE.get(), p.useBlockDescriptionPrefix().rarity(Rarity.RARE)));
    public static DeferredItem<Item> KAPPA_COMPASS = ITEMS.registerItem("kappa_compass", ItemKappaCompass::new);
    public static DeferredItem<Item> BROOM = ITEMS.registerItem("broom", ItemBroom::new);
    public static DeferredItem<Item> PICNIC_BASKET = ITEMS.registerItem("picnic_basket", p -> new ItemPicnicBasket(InitBlocks.PICNIC_MAT.get(), p));
    public static DeferredItem<Item> SCARECROW = ITEMS.registerItem("scarecrow", p -> new BlockItem(InitBlocks.SCARECROW.get(), p.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> SERVANT_BELL = ITEMS.registerItem("servant_bell", ItemServantBell::new, p -> p.stacksTo(1));
    public static DeferredItem<Item> ENTITY_ID_COPY = ITEMS.registerItem("entity_id_copy", ItemEntityIdCopy::new);
    public static DeferredItem<Item> OWNER_CONVERSION_TOOL = ITEMS.registerItem("owner_conversion_tool", p -> new Item(p.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> GOMOKU_BOARD_STATE = ITEMS.registerItem("gomoku_board_state", ItemBoardState::new);
    public static DeferredItem<Item> CCHESS_BOARD_STATE = ITEMS.registerItem("cchess_board_state", ItemBoardState::new);
    public static DeferredItem<Item> WCHESS_BOARD_STATE = ITEMS.registerItem("wchess_board_state", ItemBoardState::new);
    public static DeferredItem<Item> SNACK_CABINET = ITEMS.registerItem("snack_cabinet", p -> new BlockItem(InitBlocks.SNACK_CABINET.get(), p.useBlockDescriptionPrefix()));
    @SuppressWarnings("deprecation")
    public static DeferredItem<Item> MONSTER_LIST = ITEMS.registerItem("monster_list", ItemMonsterList::new);

    public static DeferredItem<Item> MAID_SPAWN_EGG = ITEMS.registerItem("maid_spawn_egg", p -> new SpawnEggItem(p));
    public static DeferredItem<Item> FAIRY_SPAWN_EGG = ITEMS.registerItem("fairy_spawn_egg", ItemFairySpawnEgg::new);

    public static final Identifier MEMORIZABLE_GENSOKYO_LOCATION = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");

    // 成就图标
    public static DeferredItem<Item> CHANGE_CHAIR_MODEL = ITEMS.registerItem("change_chair_model", ItemAdvancementIcon::new);
    public static DeferredItem<Item> CHANGE_MAID_MODEL = ITEMS.registerItem("change_maid_model", ItemAdvancementIcon::new);
    public static DeferredItem<Item> MAID_100_HEALTHY = ITEMS.registerItem("maid_100_healthy", ItemAdvancementIcon::new);
    public static DeferredItem<Item> KILL_100 = ITEMS.registerItem("kill_100", ItemAdvancementIcon::new);
    public static DeferredItem<Item> KILL_SLIME_300 = ITEMS.registerItem("kill_slime_300", ItemAdvancementIcon::new);
    public static DeferredItem<Item> ALL_NETHERITE_EQUIPMENT = ITEMS.registerItem("all_netherite_equipment", ItemAdvancementIcon::new);
    public static DeferredItem<Item> KILL_WITHER = ITEMS.registerItem("kill_wither", ItemAdvancementIcon::new);
    public static DeferredItem<Item> KILL_DRAGON = ITEMS.registerItem("kill_dragon", ItemAdvancementIcon::new);
    public static DeferredItem<Item> TACZ_GUN_ICON = ITEMS.registerItem("tacz_gun_icon", ItemAdvancementIcon::new);
}
