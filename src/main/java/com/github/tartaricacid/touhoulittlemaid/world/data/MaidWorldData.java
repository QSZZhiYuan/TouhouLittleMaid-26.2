package com.github.tartaricacid.touhoulittlemaid.world.data;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.util.ComponentSerializerHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MaidWorldData extends SavedData {
    private static final String IDENTIFIER = "touhou_little_maid_world_data";
    private static final String MAID_INFOS_TAG = "MaidInfos";
    private static final String MAID_TOMBSTONES_TAG = "MaidTombstones";
    private final Map<UUID, List<MaidInfo>> infos = Maps.newHashMap();
    private final Map<UUID, List<MaidInfo>> tombstones = Maps.newHashMap();

    public static SavedDataType<MaidWorldData> factory() {
        // TODO: 26.2 中 SavedData.Factory 被移除，改用 SavedDataType
        // 需要提供 Codec<MaidWorldData>，此处使用简化实现
        return new SavedDataType<>(
                Identifier.parse(IDENTIFIER),
                MaidWorldData::new,
                CompoundTag.CODEC.xmap(
                        tag -> MaidWorldData.load(tag, HolderLookup.Provider.create(java.util.stream.Stream.empty())),
                        data -> {
                            CompoundTag tag = new CompoundTag();
                            data.save(tag, HolderLookup.Provider.create(java.util.stream.Stream.empty()));
                            return tag;
                        }
                ),
                DataFixTypes.ENTITY_CHUNK
        );
    }

    @Nullable
    public static MaidWorldData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel overWorld = level.getServer().getLevel(Level.OVERWORLD);
            if (overWorld == null) {
                return null;
            }
            SavedDataStorage storage = overWorld.getDataStorage();
            MaidWorldData data = storage.computeIfAbsent(MaidWorldData.factory());
            data.setDirty();
            return data;
        }
        return null;
    }

    public static MaidWorldData load(CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        MaidWorldData data = new MaidWorldData();
        if (tag.contains(MAID_INFOS_TAG)) {
            CompoundTag infosTag = tag.getCompoundOrEmpty(MAID_INFOS_TAG);
            for (String key : infosTag.keySet()) {
                ListTag listTag = infosTag.getListOrEmpty(key);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag infoTag = listTag.getCompoundOrEmpty(i);
                    String dimension = infoTag.getStringOr("Dimension", "");
                    @Nullable BlockPos chunkPos = infoTag.read("ChunkPos", BlockPos.CODEC).orElse(null);
                    if (chunkPos == null) {
                        continue;
                    }
                    UUID ownerId = infoTag.read("OwnerId", UUIDUtil.CODEC).orElse(Util.NIL_UUID);
                    UUID maidId = infoTag.read("MaidId", UUIDUtil.CODEC).orElse(Util.NIL_UUID);
                    long timestamp = infoTag.getLongOr("Timestamp", 0);
                    MutableComponent name = ComponentSerializerHelper.fromJson(infoTag.getStringOr("Name", ""), provider);
                    List<MaidInfo> maidInfos = data.infos.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    maidInfos.add(new MaidInfo(dimension, chunkPos, ownerId, maidId, timestamp, name));
                }
            }
        }
        if (tag.contains(MAID_TOMBSTONES_TAG)) {
            CompoundTag tombstonesTag = tag.getCompoundOrEmpty(MAID_TOMBSTONES_TAG);
            for (String key : tombstonesTag.keySet()) {
                ListTag listTag = tombstonesTag.getListOrEmpty(key);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag infoTag = listTag.getCompoundOrEmpty(i);
                    String dimension = infoTag.getStringOr("Dimension", "");
                    @Nullable BlockPos chunkPos = infoTag.read("ChunkPos", BlockPos.CODEC).orElse(null);
                    if (chunkPos == null) {
                        continue;
                    }
                    UUID ownerId = infoTag.read("OwnerId", UUIDUtil.CODEC).orElse(Util.NIL_UUID);
                    UUID tombstoneId = infoTag.read("TombstoneId", UUIDUtil.CODEC).orElse(Util.NIL_UUID);
                    long timestamp = infoTag.getLongOr("Timestamp", 0);
                    MutableComponent name = ComponentSerializerHelper.fromJson(infoTag.getStringOr("Name", ""), provider);
                    List<MaidInfo> tombstoneInfos = data.tombstones.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    tombstoneInfos.add(new MaidInfo(dimension, chunkPos, ownerId, tombstoneId, timestamp, name));
                }
            }
        }
        return data;
    }


    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag infosTag = new CompoundTag();
        infos.forEach((id, data) -> {
            ListTag listTag = new ListTag();
            data.forEach(info -> {
                if (info.getChunkPos() == null) {
                    return;
                }
                CompoundTag infoTag = new CompoundTag();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", (Tag) BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, info.getChunkPos()).result().orElse(null));
                infoTag.store("OwnerId", UUIDUtil.CODEC, info.getOwnerId());
                infoTag.store("MaidId", UUIDUtil.CODEC, info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", ComponentSerializerHelper.toJson(info.getName(), provider));
                listTag.add(infoTag);
            });
            infosTag.put(id.toString(), listTag);
        });

        CompoundTag tombstonesTag = new CompoundTag();
        tombstones.forEach((id, data) -> {
            ListTag listTag = new ListTag();
            data.forEach(info -> {
                if (info.getChunkPos() == null) {
                    return;
                }
                CompoundTag infoTag = new CompoundTag();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", (Tag) BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, info.getChunkPos()).result().orElse(null));
                infoTag.store("OwnerId", UUIDUtil.CODEC, info.getOwnerId());
                infoTag.store("TombstoneId", UUIDUtil.CODEC, info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", ComponentSerializerHelper.toJson(info.getName(), provider));
                listTag.add(infoTag);
            });
            tombstonesTag.put(id.toString(), listTag);
        });
        tag.put(MAID_INFOS_TAG, infosTag);
        tag.put(MAID_TOMBSTONES_TAG, tombstonesTag);
        return tag;
    }

    public void addInfo(MaidInfo info) {
        UUID ownerId = info.getOwnerId();
        List<MaidInfo> maidInfos = this.infos.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
        maidInfos.add(info);
        this.setDirty();
    }

    public void addInfo(EntityMaid maid) {
        String dimension = maid.level.dimension().identifier().toString();
        BlockPos chunkPos = maid.blockPosition();
        UUID ownerId = maid.getOwnerReference() != null ? maid.getOwnerReference().getUUID() : Util.NIL_UUID;
        UUID maidId = maid.getUUID();
        long timestamp = System.currentTimeMillis();
        Component name = maid.getDisplayName();
        this.addInfo(new MaidInfo(dimension, chunkPos, ownerId, maidId, timestamp, name));
    }

    public void removeInfo(EntityMaid maid) {
        UUID ownerId = maid.getOwnerReference() != null ? maid.getOwnerReference().getUUID() : Util.NIL_UUID;
        if (this.infos.containsKey(ownerId)) {
            UUID maidId = maid.getUUID();
            this.infos.get(ownerId).removeIf(info -> info.getEntityId().equals(maidId));
            this.setDirty();
        }
    }

    @Nullable
    public List<MaidInfo> getInfos(UUID owner) {
        return infos.get(owner);
    }

    @Nullable
    public List<MaidInfo> getPlayerMaidInfos(Player player) {
        return this.infos.get(player.getUUID());
    }

    public void addTombstones(MaidInfo info) {
        UUID ownerId = info.getOwnerId();
        List<MaidInfo> tombstoneInfos = this.tombstones.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
        tombstoneInfos.add(info);
        this.setDirty();
    }

    public void addTombstones(EntityMaid maid, EntityTombstone tombstone) {
        String dimension = maid.level.dimension().identifier().toString();
        BlockPos chunkPos = tombstone.blockPosition();
        UUID ownerId = maid.getOwnerReference() != null ? maid.getOwnerReference().getUUID() : Util.NIL_UUID;
        UUID tombstoneId = tombstone.getUUID();
        long timestamp = System.currentTimeMillis();
        Component name = maid.getDisplayName();
        this.addTombstones(new MaidInfo(dimension, chunkPos, ownerId, tombstoneId, timestamp, name));
    }

    public void removeTombstones(EntityTombstone tombstone) {
        UUID ownerId = tombstone.getOwnerId();
        if (this.tombstones.containsKey(ownerId)) {
            UUID tombstoneId = tombstone.getUUID();
            this.tombstones.get(ownerId).removeIf(info -> info.getEntityId().equals(tombstoneId));
            this.setDirty();
        }
    }

    @Nullable
    public List<MaidInfo> getTombstones(UUID owner) {
        return tombstones.get(owner);
    }

    @Nullable
    public List<MaidInfo> getPlayerMaidTombstones(Player player) {
        return this.tombstones.get(player.getUUID());
    }
}
