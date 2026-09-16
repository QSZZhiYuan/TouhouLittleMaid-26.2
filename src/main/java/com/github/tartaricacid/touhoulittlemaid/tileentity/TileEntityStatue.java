package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Set;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityStatue extends BlockEntity {
    public static final BlockEntityType<TileEntityStatue> TYPE = new BlockEntityType<>(TileEntityStatue::new, Set.of(InitBlocks.STATUE.get()));
    private static final String STATUE_SIZE_TAG = "StatueSize";
    private static final String CORE_BLOCK_TAG = "CoreBlock";
    private static final String CORE_BLOCK_POS_TAG = "CoreBlockPos";
    private static final String STATUE_FACING_TAG = "StatueFacing";
    private static final String ALL_BLOCKS_TAG = "AllBlocks";
    private static final String EXTRA_MAID_DATA = "ExtraMaidData";
    private Size size = Size.SMALL;
    private boolean isCoreBlock = false;
    private BlockPos coreBlockPos = BlockPos.ZERO;
    private Direction facing = Direction.NORTH;
    private List<BlockPos> allBlocks = Lists.newArrayList();
    @Nullable
    private CompoundTag extraMaidData = null;

    public TileEntityStatue(BlockPos blockPos, BlockState blockState) {
        super(TYPE, blockPos, blockState);
    }

    public void setForgeData(Size size, boolean isCoreBlock, BlockPos coreBlockPos, Direction facing,
                             List<BlockPos> allBlocks, @Nullable CompoundTag extraData) {
        this.size = size;
        this.isCoreBlock = isCoreBlock;
        this.coreBlockPos = coreBlockPos;
        this.facing = facing;
        this.allBlocks = allBlocks;
        this.extraMaidData = extraData;
        refresh();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        CompoundTag tag = new CompoundTag();
        tag.putInt(STATUE_SIZE_TAG, size.ordinal());
        tag.putBoolean(CORE_BLOCK_TAG, isCoreBlock);
        tag.putIntArray(CORE_BLOCK_POS_TAG, new int[]{coreBlockPos.getX(), coreBlockPos.getY(), coreBlockPos.getZ()});
        tag.putString(STATUE_FACING_TAG, facing.getSerializedName());
        ListTag blockList = new ListTag();
        for (BlockPos pos : allBlocks) {
            blockList.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()}));
        }
        tag.put(ALL_BLOCKS_TAG, blockList);
        if (extraMaidData != null) {
            tag.put(EXTRA_MAID_DATA, extraMaidData);
        }
        output.store("StatueData", CompoundTag.CODEC, tag);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        CompoundTag tag = input.read("StatueData", CompoundTag.CODEC).orElse(new CompoundTag());
        size = Size.getSizeByIndex(tag.getIntOr(STATUE_SIZE_TAG, 0));
        isCoreBlock = tag.getBooleanOr(CORE_BLOCK_TAG, false);
        int[] posArr = tag.getIntArray(CORE_BLOCK_POS_TAG).orElse(new int[]{0, 0, 0});
        coreBlockPos = new BlockPos(posArr[0], posArr[1], posArr[2]);
        facing = Direction.byName(tag.getStringOr(STATUE_FACING_TAG, ""));
        allBlocks.clear();
        ListTag blockList = tag.getListOrEmpty(ALL_BLOCKS_TAG);
        for (int i = 0; i < blockList.size(); i++) {
            int[] pos = blockList.getIntArray(i).orElse(new int[]{0, 0, 0});
            allBlocks.add(new BlockPos(pos[0], pos[1], pos[2]));
        }
        if (tag.contains(EXTRA_MAID_DATA)) {
            extraMaidData = tag.getCompoundOrEmpty(EXTRA_MAID_DATA);
        }
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

    public Size getSize() {
        return size;
    }

    public boolean isCoreBlock() {
        return isCoreBlock;
    }

    public BlockPos getCoreBlockPos() {
        return coreBlockPos;
    }

    public Direction getFacing() {
        return facing;
    }

    public List<BlockPos> getAllBlocks() {
        return allBlocks;
    }

    @Nullable
    public CompoundTag getExtraMaidData() {
        return extraMaidData;
    }

    public enum Size {
        // 雕像的尺寸
        TINY(0.5f, new Vec3i(1, 1, 1)),
        SMALL(1.0f, new Vec3i(1, 2, 1)),
        MIDDLE(2.0f, new Vec3i(2, 4, 2)),
        BIG(3.0f, new Vec3i(3, 6, 3));

        private final float scale;
        private final Vec3i dimension;

        Size(float scale, Vec3i dimension) {
            this.scale = scale;
            this.dimension = dimension;
        }

        public static Size getSizeByIndex(int index) {
            return Size.values()[Mth.clamp(index, 0, Size.values().length - 1)];
        }

        public float getScale() {
            return scale;
        }

        public Vec3i getDimension() {
            return dimension;
        }
    }


}
