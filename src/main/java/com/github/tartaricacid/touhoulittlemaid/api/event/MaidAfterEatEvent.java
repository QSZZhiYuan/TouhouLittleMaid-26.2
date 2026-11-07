package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class MaidAfterEatEvent extends Event {
    private final EntityMaid maid;
    private final ItemStack foodAfterEat;
    private final boolean useRemainderHandled;

    public MaidAfterEatEvent(EntityMaid maid, ItemStack foodAfterEat) {
        this(maid, foodAfterEat, false);
    }

    public MaidAfterEatEvent(EntityMaid maid, ItemStack foodAfterEat, boolean useRemainderHandled) {
        this.maid = maid;
        this.foodAfterEat = foodAfterEat;
        this.useRemainderHandled = useRemainderHandled;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getFoodAfterEat() {
        return foodAfterEat;
    }

    /** True when the normal item-use pipeline has already processed USE_REMAINDER. */
    public boolean isUseRemainderHandled() {
        return useRemainderHandled;
    }
}
