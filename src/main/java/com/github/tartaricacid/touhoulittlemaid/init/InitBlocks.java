package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.*;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TouhouLittleMaid.MOD_ID);

    public static DeferredBlock<Block> MAID_BED = BLOCKS.registerBlock("maid_bed", BlockMaidBed::new, p -> p.sound(SoundType.WOOD).strength(0.2F).noOcclusion());
    public static DeferredBlock<Block> ALTAR = BLOCKS.registerBlock("altar", BlockAltar::new, p -> p.sound(SoundType.STONE).strength(2, 2).noOcclusion());
    public static DeferredBlock<Block> STATUE = BLOCKS.registerBlock("statue", BlockStatue::new, p -> p.sound(SoundType.MUD).strength(1, 2).noOcclusion());
    public static DeferredBlock<Block> GARAGE_KIT = BLOCKS.registerBlock("garage_kit", BlockGarageKit::new, p -> p.sound(SoundType.MUD).strength(1, 2).noOcclusion());
    public static DeferredBlock<Block> MAID_BEACON = BLOCKS.registerBlock("maid_beacon", BlockMaidBeacon::new, p -> p.sound(SoundType.WOOD).strength(2, 2).noOcclusion().lightLevel(s -> s.getValue(BlockMaidBeacon.POSITION) == BlockMaidBeacon.Position.DOWN ? 0 : 15));
    public static DeferredBlock<Block> MODEL_SWITCHER = BLOCKS.registerBlock("model_switcher", BlockModelSwitcher::new, p -> p.sound(SoundType.STONE).strength(50.0F, 1200.0F));
    public static DeferredBlock<Block> PICNIC_MAT = BLOCKS.registerBlock("picnic_mat", BlockPicnicMat::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> GOMOKU = BLOCKS.registerBlock("gomoku", BlockGomoku::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> CCHESS = BLOCKS.registerBlock("cchess", BlockCChess::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> WCHESS = BLOCKS.registerBlock("wchess", BlockWChess::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> KEYBOARD = BLOCKS.registerBlock("keyboard", BlockKeyboard::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> BOOKSHELF = BLOCKS.registerBlock("bookshelf", BlockBookshelf::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> COMPUTER = BLOCKS.registerBlock("computer", BlockComputer::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
    public static DeferredBlock<Block> SHRINE = BLOCKS.registerBlock("shrine", BlockShrine::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion());
    public static DeferredBlock<Block> SCARECROW = BLOCKS.registerBlock("scarecrow", BlockScarecrow::new, p -> p.sound(SoundType.GRASS).strength(0.2F).noOcclusion());
    public static DeferredBlock<Block> SNACK_CABINET = BLOCKS.registerBlock("snack_cabinet", BlockSnackCabinet::new, p -> p.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion());

    public static Supplier<BlockEntityType<TileEntityAltar>> ALTAR_TE = TILE_ENTITIES.register("altar", () -> TileEntityAltar.TYPE);
    public static Supplier<BlockEntityType<TileEntityStatue>> STATUE_TE = TILE_ENTITIES.register("statue", () -> TileEntityStatue.TYPE);
    public static Supplier<BlockEntityType<TileEntityGarageKit>> GARAGE_KIT_TE = TILE_ENTITIES.register("garage_kit", () -> TileEntityGarageKit.TYPE);
    public static Supplier<BlockEntityType<TileEntityMaidBeacon>> MAID_BEACON_TE = TILE_ENTITIES.register("maid_beacon", () -> TileEntityMaidBeacon.TYPE);
    public static Supplier<BlockEntityType<TileEntityModelSwitcher>> MODEL_SWITCHER_TE = TILE_ENTITIES.register("model_switcher", () -> TileEntityModelSwitcher.TYPE);
    public static Supplier<BlockEntityType<TileEntityGomoku>> GOMOKU_TE = TILE_ENTITIES.register("gomoku", () -> TileEntityGomoku.TYPE);
    public static Supplier<BlockEntityType<TileEntityCChess>> CCHESS_TE = TILE_ENTITIES.register("cchess", () -> TileEntityCChess.TYPE);
    public static Supplier<BlockEntityType<TileEntityWChess>> WCHESS_TE = TILE_ENTITIES.register("wchess", () -> TileEntityWChess.TYPE);
    public static Supplier<BlockEntityType<TileEntityKeyboard>> KEYBOARD_TE = TILE_ENTITIES.register("keyboard", () -> TileEntityKeyboard.TYPE);
    public static Supplier<BlockEntityType<TileEntityBookshelf>> BOOKSHELF_TE = TILE_ENTITIES.register("bookshelf", () -> TileEntityBookshelf.TYPE);
    public static Supplier<BlockEntityType<TileEntityComputer>> COMPUTER_TE = TILE_ENTITIES.register("computer", () -> TileEntityComputer.TYPE);
    public static Supplier<BlockEntityType<TileEntityShrine>> SHRINE_TE = TILE_ENTITIES.register("shrine", () -> TileEntityShrine.TYPE);
    public static Supplier<BlockEntityType<TileEntityPicnicMat>> PICNIC_MAT_TE = TILE_ENTITIES.register("picnic_mat", () -> TileEntityPicnicMat.TYPE);
    public static Supplier<BlockEntityType<TileEntityMaidBed>> MAID_BED_TE = TILE_ENTITIES.register("maid_bed", () -> TileEntityMaidBed.TYPE);
    public static Supplier<BlockEntityType<TileEntitySnackCabinet>> SNACK_CABINET_TE = TILE_ENTITIES.register("snack_cabinet", () -> TileEntitySnackCabinet.TYPE);
}
