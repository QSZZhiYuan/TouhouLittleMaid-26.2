package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.ValueInput;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/** Reads ownership without resolving an online player or changing the saved owner. */
public final class MaidOwnerData {
    private static final String OWNER = "Owner";
    private static final String OWNER_BACKUP = "MaidOwnerUUID";

    private MaidOwnerData() {
    }

    @SuppressWarnings("deprecation")
    public static CompoundTag prepareForLoad(ValueInput input) {
        CompoundTag data = input.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC))
                .orElseGet(CompoundTag::new).copy();
        // Normalize before vanilla tries its old player-name conversion or clears the tamed flag.
        Optional<UUID> owner = read(data);
        if (owner.isPresent()) {
            data.store(OWNER, UUIDUtil.CODEC, owner.get());
        } else if (parseUuid(data, OWNER).filter(Util.NIL_UUID::equals).isPresent()) {
            // The all-zero UUID cannot identify a player; do not reload it as a tamed owner.
            data.remove(OWNER);
        }
        return data;
    }

    public static Optional<UUID> read(CompoundTag data) {
        return readRoot(data).or(() -> readRoot(data.getCompoundOrEmpty("maid_data")));
    }

    private static Optional<UUID> readRoot(CompoundTag data) {
        // A valid vanilla Owner always wins, including edits made by conversion events.
        return readUuid(data, OWNER).or(() -> readUuid(data, OWNER_BACKUP));
    }

    private static Optional<UUID> readUuid(CompoundTag data, String key) {
        return parseUuid(data, key).filter(uuid -> !Util.NIL_UUID.equals(uuid));
    }

    private static Optional<UUID> parseUuid(CompoundTag data, String key) {
        return Optional.ofNullable(data.get(key))
                .flatMap(tag -> UUIDUtil.LENIENT_CODEC.parse(NbtOps.INSTANCE, tag).result());
    }

    public static void storeBackup(CompoundTag data, @Nullable UUID owner) {
        if (owner == null || Util.NIL_UUID.equals(owner)) {
            data.remove(OWNER_BACKUP);
        } else {
            data.store(OWNER_BACKUP, UUIDUtil.CODEC, owner);
        }
    }
}
