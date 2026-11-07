package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.AltarItemHandler;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Set;

public class TileEntityAltar extends BlockEntity {
    public static final BlockEntityType<TileEntityAltar> TYPE = new BlockEntityType<>(TileEntityAltar::new, Set.of(InitBlocks.ALTAR.get()));
    private static final String STORAGE_ITEM = "StorageItem";
    private static final String IS_RENDER = "IsRender";
    private static final String CAN_PLACE_ITEM = "CanPlaceItem";
    private static final String STORAGE_STATE_ID = "StorageBlockStateId";
    private static final String DIRECTION = "Direction";
    private static final String STORAGE_BLOCK_LIST = "StorageBlockList";
    private static final String CAN_PLACE_ITEM_POS_LIST = "CanPlaceItemPosList";
    public final ItemStackHandler handler = new AltarItemHandler();
    private boolean isRender = false;
    private boolean canPlaceItem = false;
    private BlockState storageState = Blocks.AIR.defaultBlockState();
    private PosListData blockPosList = new PosListData();
    private PosListData canPlaceItemPosList = new PosListData();
    private Direction direction = Direction.SOUTH;

    public TileEntityAltar(BlockPos blockPos, BlockState blockState) {
        super(TYPE, blockPos, blockState);
    }

    public void setForgeData(BlockState storageState, boolean isRender, boolean canPlaceItem, Direction direction,
                             PosListData blockPosList, PosListData canPlaceItemPosList) {
        this.isRender = isRender;
        this.canPlaceItem = canPlaceItem;
        this.storageState = storageState;
        this.direction = direction;
        this.blockPosList = blockPosList;
        this.canPlaceItemPosList = canPlaceItemPosList;
        refresh();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(IS_RENDER, isRender);
        tag.putBoolean(CAN_PLACE_ITEM, canPlaceItem);
        tag.putInt(STORAGE_STATE_ID, Block.getId(storageState));
        // Serialize through the supplied registry-aware output: a block entity can be saved before a level is attached.
        tag.putString(DIRECTION, direction.getSerializedName());
        tag.put(STORAGE_BLOCK_LIST, blockPosList.serialize());
        tag.put(CAN_PLACE_ITEM_POS_LIST, canPlaceItemPosList.serialize());
        ValueOutput altarOutput = output.child("AltarData");
        altarOutput.store(tag);
        handler.serialize(altarOutput.child(STORAGE_ITEM));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        CompoundTag tag = input.read("AltarData", CompoundTag.CODEC).orElse(new CompoundTag());
        isRender = tag.getBooleanOr(IS_RENDER, false);
        canPlaceItem = tag.getBooleanOr(CAN_PLACE_ITEM, false);
        storageState = Block.stateById(tag.getIntOr(STORAGE_STATE_ID, 0));
        // Keep reading the nested inventory written by earlier 26.2 builds.
        ValueInput handlerInput = input.child(STORAGE_ITEM).orElseGet(() ->
                TagValueInput.create(ProblemReporter.DISCARDING, input.lookup(), tag.getCompoundOrEmpty(STORAGE_ITEM)));
        handler.deserialize(handlerInput);
        Direction savedDirection = Direction.byName(tag.getStringOr(DIRECTION, "south"));
        direction = savedDirection != null && savedDirection.getAxis().isHorizontal() ? savedDirection : Direction.SOUTH;
        blockPosList.deserialize(tag.getListOrEmpty(STORAGE_BLOCK_LIST));
        canPlaceItemPosList.deserialize(tag.getListOrEmpty(CAN_PLACE_ITEM_POS_LIST));
    }

    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public boolean isRender() {
        return isRender;
    }

    public boolean isCanPlaceItem() {
        return canPlaceItem;
    }

    public BlockState getStorageState() {
        return storageState;
    }

    public PosListData getBlockPosList() {
        return blockPosList;
    }

    public PosListData getCanPlaceItemPosList() {
        return canPlaceItemPosList;
    }

    public ItemStack getStorageItem() {
        if (canPlaceItem) {
            return handler.getStackInSlot(0);
        }
        return ItemStack.EMPTY;
    }

    public Direction getDirection() {
        return direction;
    }
}
