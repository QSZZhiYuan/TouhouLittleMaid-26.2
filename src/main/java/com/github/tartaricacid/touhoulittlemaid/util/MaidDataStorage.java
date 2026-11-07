package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;

/** Preserves the public root-level maid data format and reads early 26.2 nested saves. */
public final class MaidDataStorage {
    private static final String NESTED_DATA = "maid_data";

    private MaidDataStorage() {
    }

    @SuppressWarnings("deprecation")
    public static CompoundTag read(ValueInput input) {
        return input.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC))
                .map(MaidDataStorage::read).orElseGet(CompoundTag::new);
    }

    public static CompoundTag read(CompoundTag entityData) {
        CompoundTag result = entityData.getCompoundOrEmpty(NESTED_DATA).copy();
        for (String key : entityData.keySet()) {
            if (!NESTED_DATA.equals(key)) {
                // Root values also preserve edits made by conversion events and legacy integrations.
                result.put(key, entityData.get(key).copy());
            }
        }
        return result;
    }
}
