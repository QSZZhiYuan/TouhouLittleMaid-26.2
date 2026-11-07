package com.github.tartaricacid.touhoulittlemaid.util;

public final class MaidItemUseTime {
    private MaidItemUseTime() {
    }

    /** Apply extra progress before vanilla subtracts its one tick, without stalling or overflowing. */
    public static int beforeVanillaTick(int remainingTicks, double speed) {
        int ticksPerTick = Math.max(1, (int) speed);
        return (int) Math.max(1L, (long) remainingTicks - (ticksPerTick - 1L));
    }
}
