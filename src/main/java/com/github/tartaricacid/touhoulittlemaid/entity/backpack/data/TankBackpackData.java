package com.github.tartaricacid.touhoulittlemaid.entity.backpack.data;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidUtil;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

public class TankBackpackData extends SimpleContainer implements IBackpackData {
    public static final int CAPACITY = 10 * FluidType.BUCKET_VOLUME;
    private static final int INPUT_INDEX = 0;
    private static final int OUTPUT_INDEX = 1;
    private final EntityMaid maid;
    private final FluidTank tank = new FluidTank(CAPACITY);
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return TankBackpackData.this.tankFluidCount;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                TankBackpackData.this.tankFluidCount = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    private int tankFluidCount = 0;

    public TankBackpackData(EntityMaid maid) {
        super(2);
        this.maid = maid;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!this.maid.level.isClientSide()) {
            CombinedInvWrapper availableInv = this.maid.getAvailableInv(false);
            if (index == INPUT_INDEX) {
                MaidFluidUtil.bucketToTank(stack, tank, availableInv);
            }
            if (index == OUTPUT_INDEX) {
                MaidFluidUtil.tankToBucket(stack, tank, availableInv);
            }
            this.tankFluidCount = tank.getFluidAmount();
            Identifier key = BuiltInRegistries.FLUID.getKey(tank.getFluid().getFluid());
            maid.setBackpackFluid(key.toString());
        }
        super.setItem(index, stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    public void load(CompoundTag tag, EntityMaid maid) {
        this.loadTank(tag.getCompoundOrEmpty("Tanks"), maid);
        ListTag itemsList = tag.getListOrEmpty("Items");
        for (int i = 0; i < itemsList.size() && i < this.getContainerSize(); i++) {
            Tag itemTag = itemsList.get(i);
            DataResult<ItemStack> result = ItemStack.OPTIONAL_CODEC.decode(NbtOps.INSTANCE, itemTag).map(p -> p.getFirst());
            this.setItem(i, result.result().orElse(ItemStack.EMPTY));
        }
    }

    @Override
    public void save(CompoundTag tag, EntityMaid maid) {
        CompoundTag tankTag = new CompoundTag();
        FluidStack fluid = tank.getFluid();
        if (!fluid.isEmpty()) {
            Tag fluidTag = FluidStack.CODEC.encodeStart(NbtOps.INSTANCE, fluid).result().orElse(null);
            if (fluidTag != null) {
                tankTag.put("Fluid", fluidTag);
            }
        }
        tag.put("Tanks", tankTag);
        ListTag itemsList = new ListTag();
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            Tag itemTag = ItemStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, stack).result().orElse(null);
            if (itemTag != null) {
                itemsList.add(itemTag);
            }
        }
        tag.put("Items", itemsList);
    }

    @Override
    public void serverTick(EntityMaid maid) {
    }

    public FluidTank getTank() {
        return tank;
    }

    public void loadTank(CompoundTag nbt, EntityMaid maid) {
        Tag fluidTag = nbt.get("Fluid");
        if (fluidTag != null) {
            DataResult<FluidStack> result = FluidStack.CODEC.decode(NbtOps.INSTANCE, fluidTag).map(p -> p.getFirst());
            tank.setFluid(result.result().orElse(FluidStack.EMPTY));
        }
        this.tankFluidCount = tank.getFluidAmount();
        Identifier key = BuiltInRegistries.FLUID.getKey(tank.getFluid().getFluid());
        maid.setBackpackFluid(key.toString());
    }
}
