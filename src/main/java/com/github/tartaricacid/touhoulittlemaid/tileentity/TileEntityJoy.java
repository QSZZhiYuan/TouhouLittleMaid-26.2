package com.github.tartaricacid.touhoulittlemaid.tileentity;

import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileEntityJoy extends BlockEntity {
    private static final String SIT_ID = "SitId";
    private UUID sitId = Util.NIL_UUID;

    public TileEntityJoy(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        CompoundTag tag = new CompoundTag();
        tag.putLong("most", this.sitId.getMostSignificantBits());
        tag.putLong("least", this.sitId.getLeastSignificantBits());
        output.store(SIT_ID, CompoundTag.CODEC, tag);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        CompoundTag tag = input.read(SIT_ID, CompoundTag.CODEC).orElse(new CompoundTag());
        long most = tag.getLongOr("most", 0);
        long least = tag.getLongOr("least", 0);
        this.sitId = new UUID(most, least);
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

    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }

    public UUID getSitId() {
        return this.sitId;
    }

    public void setSitId(UUID sitId) {
        this.sitId = sitId;
    }
}
