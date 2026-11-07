package com.github.tartaricacid.touhoulittlemaid.entity;

import com.github.tartaricacid.touhoulittlemaid.util.MaidDataStorage;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaidDataStorageTest {
    @Test
    public void readsLegacyRootHungerAndInventoryWithoutResettingThem() {
        CompoundTag saved = maidData(7);
        CompoundTag inventory = new CompoundTag();
        inventory.putInt("Size", 18);
        saved.put("MaidInventory", inventory);
        CompoundTag loaded = MaidDataStorage.read(TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, saved));
        assertEquals(7, loaded.getIntOr("MaidHunger", -1));
        assertEquals(18, loaded.getCompoundOrEmpty("MaidInventory").getIntOr("Size", -1));
    }

    @Test
    public void readsNestedEarlyPortSavesAndPreservesZeroHunger() {
        CompoundTag saved = new CompoundTag();
        saved.put("maid_data", maidData(0));
        assertEquals(0, MaidDataStorage.read(saved).getIntOr("MaidHunger", -1));
    }

    @Test
    public void rootConversionEventEditsOverrideOlderNestedValues() {
        CompoundTag saved = new CompoundTag();
        saved.put("maid_data", maidData(20));
        saved.putInt("MaidHunger", 3);
        assertEquals(3, MaidDataStorage.read(saved).getIntOr("MaidHunger", -1));
        assertEquals(20, saved.getCompoundOrEmpty("maid_data").getIntOr("MaidHunger", -1));
    }

    @Test
    public void rootOutputCanBeStoredAndReleasedRepeatedly() {
        CompoundTag saved = maidData(11);
        for (int i = 0; i < 3; i++) {
            TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
            output.store(MaidDataStorage.read(saved));
            saved = output.buildResult();
            assertFalse(saved.contains("maid_data"));
            assertEquals(11, MaidDataStorage.read(saved).getIntOr("MaidHunger", -1));
        }
    }

    private CompoundTag maidData(int hunger) {
        CompoundTag saved = new CompoundTag();
        saved.putInt("MaidHunger", hunger);
        saved.putInt("MaidFavorability", 91);
        saved.putInt("MaidExperience", 123);
        return saved;
    }
}
