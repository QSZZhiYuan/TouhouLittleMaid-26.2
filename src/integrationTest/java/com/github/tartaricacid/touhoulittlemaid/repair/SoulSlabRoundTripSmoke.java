package com.github.tartaricacid.touhoulittlemaid.repair;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidHealSelfTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidWorkMealTask;
import com.github.tartaricacid.touhoulittlemaid.init.*;
import com.github.tartaricacid.touhoulittlemaid.item.AbstractStoreMaidItem;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/** Opt-in dedicated-server integration checks; never included in the production JAR. */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class SoulSlabRoundTripSmoke {
    private static int afterEatEvents;

    @SubscribeEvent
    public static void onEat(MaidAfterEatEvent event) {
        if (event.getMaid() instanceof UseProbe) afterEatEvents++;
    }

    @SubscribeEvent
    public static void onStarted(ServerStartedEvent event) {
        try {
            ServerLevel level = event.getServer().overworld();
            roundTrip(level, 7, 11, false);
            roundTrip(level, 0, 0, false);
            roundTrip(level, 4, 13, true);
            probeEating(level);
            temporaryHands(level);
            containersAndZeroDuration(level);
            mealsDoNotInterruptUse(level);
            sittingStopsWalking(level);
            altarLoadsBeforeLevelAttachment(level);
            long recipes = event.getServer().getRecipeManager().recipeMap().byType(InitRecipes.ALTAR_CRAFTING.get()).size();
            require(recipes > 0, "altar recipes must be registered");
            TouhouLittleMaid.LOGGER.info("REPAIR_INTEGRATION PASS: soul-slab hunger, health, meal cooldowns; 28 item-use speed/hand/release cases; temporary hand recovery; containers, after-eat events, zero-duration food; meal exclusion; sitting; unattached altar; {} altar recipes", recipes);
        } catch (Throwable failure) {
            TouhouLittleMaid.LOGGER.error("REPAIR_INTEGRATION FAIL", failure);
        } finally {
            event.getServer().halt(false);
        }
    }

    private static void roundTrip(ServerLevel level, int hunger, double attributeHunger, boolean legacyNested) {
        var owner = FakePlayerFactory.getMinecraft(level);
        EntityMaid maid = new EntityMaid(level);
        maid.tame(owner);
        maid.setHunger(hunger);
        maid.getAttribute(InitAttribute.MAID_HUNGER).setBaseValue(attributeHunger);
        maid.getMaidInv().setStackInSlot(0, new ItemStack(Items.DIAMOND, 3));
        maid.setHealth(13);
        maid.getFavorabilityManager().apply(Type.WORK_MEAL, 0);
        maid.getFavorabilityManager().apply(Type.HOME_MEAL, 0);
        for (int cycle = 0; cycle < 3; cycle++) {
            ItemStack slab = InitItems.SMART_SLAB_HAS_MAID.get().getDefaultInstance();
            AbstractStoreMaidItem.storeMaidData(slab, maid);
            CompoundTag saved = slab.get(InitDataComponent.MAID_INFO).copyTag();
            require(saved.getIntOr("MaidHunger", -1) == hunger, "root hunger at store");
            if (legacyNested && cycle == 0) {
                CompoundTag nested = new CompoundTag();
                for (String key : new String[]{"MaidHunger", "MaidInventory", "FavorabilityManagerCounter"}) {
                    nested.put(key, saved.get(key).copy());
                    saved.remove(key);
                }
                saved.put("maid_data", nested);
                slab.set(InitDataComponent.MAID_INFO, CustomData.of(saved));
            }
            maid.discard();
            owner.setItemInHand(InteractionHand.MAIN_HAND, slab);
            EntityMaid released = new EntityMaid(level);
            boolean[] spawned = {false};
            var hit = new BlockHitResult(new Vec3(0.5, 65, 0.5), Direction.UP, new BlockPos(0, 64, 0), false);
            var context = new UseOnContext(owner, InteractionHand.MAIN_HAND, hit);
            ((AbstractStoreMaidItem) slab.getItem()).spawnFromStore(context, owner, level, released, () -> spawned[0] = true);
            require(spawned[0], "release handler must complete");
            require(released.getHunger() == hunger, "hunger after release");
            require(released.getAttributeValue(InitAttribute.MAID_HUNGER) == attributeHunger, "attribute hunger after release");
            require(released.getHealth() == 13, "health after release");
            require(!released.getFavorabilityManager().canAdd(Type.WORK_MEAL.getTypeName()), "work meal cooldown after release");
            require(!released.getFavorabilityManager().canAdd(Type.HOME_MEAL.getTypeName()), "home meal cooldown after release");
            require(released.getMaidInv().getStackInSlot(0).is(Items.DIAMOND)
                    && released.getMaidInv().getStackInSlot(0).getCount() == 3, "inventory after release");
            maid = released;
        }
        maid.discard();
    }

    private static void probeEating(ServerLevel level) {
        for (double speed : new double[]{1, 0, -1, 0.5, 2, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            for (InteractionHand hand : InteractionHand.values()) {
                for (boolean soulSlab : new boolean[]{false, true}) {
                    UseProbe maid = new UseProbe(level);
                    maid.getAttribute(InitAttribute.MAID_USE_ITEM_SPEED).setBaseValue(speed);
                    maid.setItemInHand(hand, new ItemStack(Items.BREAD, 3));
                    maid.setItemInHand(other(hand), new ItemStack(Items.TORCH));
                    if (soulSlab) maid = release(level, maid);
                    require(maid.getAttributeValue(InitAttribute.MAID_USE_ITEM_SPEED) == speed, "speed preserved on release");
                    int before = afterEatEvents;
                    maid.startUsingItem(hand);
                    int ticks = finish(maid);
                    require(!maid.isUsingItem() && maid.getItemInHand(hand).getCount() == 2, "meal must end and consume once at speed " + speed);
                    require(maid.getItemInHand(other(hand)).is(Items.TORCH), "other hand must stay equipped");
                    require(afterEatEvents == before + 1, "one after-eat event per completed meal");
                    require(ticks == (speed >= 2 ? Math.max(1, (int) Math.ceil(32 / speed)) : 32), "expected meal duration at speed " + speed);
                    maid.discard();
                }
            }
        }
    }

    private static void temporaryHands(ServerLevel level) {
        for (InteractionHand hand : InteractionHand.values()) {
            for (boolean empty : new boolean[]{false, true}) {
                for (int end = 0; end < 3; end++) {
                    UseProbe maid = new UseProbe(level);
                    ItemStack saved = empty ? ItemStack.EMPTY : new ItemStack(Items.IRON_SWORD);
                    maid.setItemInHand(hand, new ItemStack(Items.BREAD, 3));
                    maid.setItemInHand(other(hand), new ItemStack(Items.TORCH));
                    maid.memoryHandItemStack(saved, hand);
                    maid.startUsingItem(hand);
                    if (end == 0) finish(maid);
                    else if (end == 1) maid.stopUsingItem();
                    else maid = release(level, maid);
                    require(ItemStack.isSameItemSameComponents(saved, maid.getItemInHand(hand)), "temporary hand restored on finish/cancel/release");
                    require(maid.getItemInHand(other(hand)).is(Items.TORCH), "temporary meal must preserve other hand");
                    require(maid.getHideInv().getStackInSlot(0).isEmpty(), "hidden slot recovered");
                    require(count(maid, Items.BREAD) == (end == 0 ? 2 : 3), "temporary meal food count");
                    maid.discard();
                }
            }
        }
    }

    private static void containersAndZeroDuration(ServerLevel level) {
        for (Item food : new Item[]{Items.HONEY_BOTTLE, Items.MUSHROOM_STEW}) {
            for (int initial : new int[]{1, 3}) {
                UseProbe maid = new UseProbe(level);
                maid.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(food, initial));
                int before = afterEatEvents;
                maid.startUsingItem(InteractionHand.MAIN_HAND);
                finish(maid);
                require(count(maid, food) == initial - 1, "container food consumed once");
                require(count(maid, food == Items.HONEY_BOTTLE ? Items.GLASS_BOTTLE : Items.BOWL) == 1, "exactly one use remainder, including stacked food");
                require(afterEatEvents == before + 1, "container food event once");
                maid.discard();
            }
        }
        UseProbe maid = new UseProbe(level);
        ItemStack instantFood = new ItemStack(Items.BREAD, 2);
        instantFood.set(DataComponents.CONSUMABLE, Consumable.builder().consumeSeconds(0).build());
        maid.setItemInHand(InteractionHand.MAIN_HAND, instantFood);
        maid.startUsingItem(InteractionHand.MAIN_HAND);
        require(finish(maid) == 1 && maid.getMainHandItem().getCount() == 1, "zero-duration consumable must complete");
        maid.discard();
    }

    private static void mealsDoNotInterruptUse(ServerLevel level) {
        UseProbe maid = new UseProbe(level);
        maid.setHealth(5);
        maid.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BREAD, 3));
        maid.startUsingItem(InteractionHand.MAIN_HAND);
        var heal = new HealProbe();
        var work = new WorkProbe();
        for (int i = 0; i < 110; i++) {
            require(!heal.allowed(level, maid), "heal meal must not interrupt active use");
            require(!work.allowed(level, maid), "work meal must not interrupt active use");
        }
        maid.stopUsingItem();
        maid.discard();
    }

    private static UseProbe release(ServerLevel level, UseProbe maid) {
        var owner = FakePlayerFactory.getMinecraft(level);
        maid.tame(owner);
        ItemStack slab = InitItems.SMART_SLAB_HAS_MAID.get().getDefaultInstance();
        AbstractStoreMaidItem.storeMaidData(slab, maid);
        maid.discard();
        owner.setItemInHand(InteractionHand.MAIN_HAND, slab);
        UseProbe released = new UseProbe(level);
        var hit = new BlockHitResult(new Vec3(0.5, 65, 0.5), Direction.UP, new BlockPos(0, 64, 0), false);
        ((AbstractStoreMaidItem) slab.getItem()).spawnFromStore(new UseOnContext(owner, InteractionHand.MAIN_HAND, hit), owner, level, released, () -> {});
        return released;
    }

    private static InteractionHand other(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    private static int count(EntityMaid maid, Item item) {
        int count = 0;
        var inv = maid.getAvailableInv(false);
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.is(item)) count += stack.getCount();
        }
        return count;
    }

    private static int finish(UseProbe maid) {
        int ticks = 0;
        while (maid.isUsingItem() && ticks < 80) {
            maid.advance();
            ticks++;
        }
        require(!maid.isUsingItem(), "use must finish within 80 ticks");
        return ticks;
    }

    private static final class HealProbe extends MaidHealSelfTask {
        boolean allowed(ServerLevel level, EntityMaid maid) { return super.checkExtraStartConditions(level, maid); }
    }

    private static final class WorkProbe extends MaidWorkMealTask {
        boolean allowed(ServerLevel level, EntityMaid maid) { return super.checkExtraStartConditions(level, maid); }
    }

    private static final class UseProbe extends EntityMaid {
        UseProbe(ServerLevel level) { super(level); }
        void advance() { super.updateUsingItem(getUseItem()); }
    }

    private static void sittingStopsWalking(ServerLevel level) {
        EntityMaid maid = new EntityMaid(level);
        maid.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPos(4, 65, 4), 1, 0));
        maid.getMoveControl().setWantedPosition(4, 65, 4, 1);
        maid.setInSittingPose(true);
        maid.getMoveControl().tick();
        require(maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty(), "sitting walk target cleared");
        require(!maid.getMoveControl().hasWanted() && maid.getSpeed() == 0, "sitting movement stopped");
        maid.discard();
    }

    private static void altarLoadsBeforeLevelAttachment(ServerLevel level) {
        var state = InitBlocks.ALTAR.get().defaultBlockState();
        TileEntityAltar altar = new TileEntityAltar(BlockPos.ZERO, state);
        altar.setForgeData(Blocks.OAK_LOG.defaultBlockState(), true, true, Direction.EAST, new PosListData(), new PosListData());
        altar.handler.setStackInSlot(0, new ItemStack(Items.DIAMOND, 2));
        CompoundTag saved = altar.saveWithoutMetadata(level.registryAccess());
        TileEntityAltar loaded = new TileEntityAltar(BlockPos.ZERO, state);
        loaded.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), saved));
        require(loaded.getLevel() == null && loaded.isRender(), "altar renderer flag before level attached");
        require(loaded.getDirection() == Direction.EAST && loaded.getStorageItem().getCount() == 2, "altar direction and inventory");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
