package com.github.tartaricacid.touhoulittlemaid.entity;

import com.github.tartaricacid.touhoulittlemaid.util.MaidOwnerData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.TagValueInput;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class MaidOwnerDataTest {
    private static final UUID FIRST = UUID.fromString("bde9e9cd-2015-4174-9401-f2c04597fc18");
    private static final UUID SECOND = UUID.fromString("9989bd2d-0c40-4018-8b76-f791c7d4ce39");

    @Test
    public void readsVanillaOwnerAndLegacyUuidString() {
        CompoundTag saved = new CompoundTag();
        saved.store("Owner", UUIDUtil.CODEC, FIRST);
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
        saved.putString("Owner", FIRST.toString());
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
    }

    @Test
    public void currentOwnerOverridesStaleBackupAndNestedOwner() {
        CompoundTag saved = new CompoundTag();
        MaidOwnerData.storeBackup(saved, FIRST);
        saved.store("Owner", UUIDUtil.CODEC, SECOND);
        CompoundTag nested = new CompoundTag();
        nested.store("Owner", UUIDUtil.CODEC, FIRST);
        saved.put("maid_data", nested);
        assertEquals(SECOND, MaidOwnerData.read(saved).orElseThrow());
    }

    @Test
    public void restoresMissingMalformedOrNilOwnerFromBackup() {
        CompoundTag saved = new CompoundTag();
        MaidOwnerData.storeBackup(saved, FIRST);
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
        saved.putString("Owner", "not-a-uuid");
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
        saved.store("Owner", UUIDUtil.CODEC, Util.NIL_UUID);
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
    }

    @Test
    public void readsEarlyPortNestedOwner() {
        CompoundTag saved = new CompoundTag();
        CompoundTag nested = new CompoundTag();
        nested.store("Owner", UUIDUtil.CODEC, FIRST);
        saved.put("maid_data", nested);
        assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
    }

    @Test
    public void rootBackupWinsOverAnObsoleteNestedOwner() {
        CompoundTag saved = new CompoundTag();
        MaidOwnerData.storeBackup(saved, SECOND);
        CompoundTag nested = new CompoundTag();
        nested.store("Owner", UUIDUtil.CODEC, FIRST);
        saved.put("maid_data", nested);
        assertEquals(SECOND, MaidOwnerData.read(saved).orElseThrow());
    }

    @Test
    public void normalizesOwnerBeforeVanillaLoadWithoutMutatingSavedData() {
        CompoundTag saved = new CompoundTag();
        saved.putString("Owner", "not-a-uuid");
        MaidOwnerData.storeBackup(saved, SECOND);
        CompoundTag nested = new CompoundTag();
        nested.store("Owner", UUIDUtil.CODEC, FIRST);
        saved.put("maid_data", nested);
        saved.putInt("MaidHunger", 7);
        CompoundTag original = saved.copy();
        CompoundTag loaded = MaidOwnerData.prepareForLoad(
                TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, saved));
        assertEquals(SECOND, loaded.read("Owner", UUIDUtil.CODEC).orElseThrow());
        assertEquals(7, loaded.getIntOr("MaidHunger", -1));
        assertEquals(nested, loaded.getCompoundOrEmpty("maid_data"));
        assertEquals(original, saved);
    }

    @Test
    public void vanillaOwnerReaderReceivesTheRecoveredUuid() {
        CompoundTag saved = new CompoundTag();
        MaidOwnerData.storeBackup(saved, FIRST);
        for (int i = 0; i < 3; i++) {
            saved.remove("Owner");
            CompoundTag loaded = MaidOwnerData.prepareForLoad(
                    TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, saved));
            var reference = EntityReference.read(
                    TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, loaded), "Owner");
            assertNotNull(reference);
            assertEquals(FIRST, reference.getUUID());
            TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
            reference.store(output, "Owner");
            saved = output.buildResult();
            MaidOwnerData.storeBackup(saved, reference.getUUID());
        }
    }

    @Test
    public void legacyPlayerNamesRemainAvailableToVanillaConversion() {
        CompoundTag saved = new CompoundTag();
        saved.putString("Owner", "legacy_test_owner");
        CompoundTag loaded = MaidOwnerData.prepareForLoad(
                TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, saved));
        assertEquals("legacy_test_owner", loaded.getStringOr("Owner", ""));
    }

    @Test
    public void neverInventsAnOwnerForWildOrDamagedData() {
        CompoundTag saved = new CompoundTag();
        assertTrue(MaidOwnerData.read(saved).isEmpty());
        saved.putString("Owner", "not-a-uuid");
        assertTrue(MaidOwnerData.read(saved).isEmpty());
        saved.store("Owner", UUIDUtil.CODEC, Util.NIL_UUID);
        assertTrue(MaidOwnerData.read(saved).isEmpty());
        CompoundTag loaded = MaidOwnerData.prepareForLoad(
                TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, saved));
        assertFalse(loaded.contains("Owner"));
    }

    @Test
    public void backupSurvivesRepeatedSavingAndOwnerChanges() {
        CompoundTag saved = new CompoundTag();
        for (int i = 0; i < 3; i++) {
            MaidOwnerData.storeBackup(saved, FIRST);
            TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
            output.store(saved);
            saved = output.buildResult();
            assertEquals(FIRST, MaidOwnerData.read(saved).orElseThrow());
        }
        MaidOwnerData.storeBackup(saved, SECOND);
        assertEquals(SECOND, MaidOwnerData.read(saved).orElseThrow());
        MaidOwnerData.storeBackup(saved, null);
        assertTrue(MaidOwnerData.read(saved).isEmpty());
        MaidOwnerData.storeBackup(saved, Util.NIL_UUID);
        assertTrue(MaidOwnerData.read(saved).isEmpty());
    }
}
