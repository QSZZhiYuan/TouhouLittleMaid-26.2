package com.github.tartaricacid.touhoulittlemaid.entity;

import com.github.tartaricacid.touhoulittlemaid.util.MaidItemUseTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaidItemUseTimeTest {
    @Test
    public void defaultAndAcceleratedUseKeepTheirDuration() {
        assertEquals(32, duration(32, 1));
        assertEquals(16, duration(32, 2));
        assertEquals(11, duration(32, 3));
    }

    @Test
    public void nonpositiveFractionalAndNanSpeedCannotStallUse() {
        for (double speed : new double[]{0, -1, 0.5, Double.NaN, Integer.MIN_VALUE, Double.NEGATIVE_INFINITY}) {
            assertEquals(32, duration(32, speed));
        }
    }

    @Test
    public void extremeSpeedAndCountersCannotOverflow() {
        assertEquals(1, duration(32, Integer.MAX_VALUE));
        assertEquals(1, duration(32, Double.POSITIVE_INFINITY));
        assertEquals(1, MaidItemUseTime.beforeVanillaTick(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, MaidItemUseTime.beforeVanillaTick(Integer.MAX_VALUE, -1));
    }

    private int duration(int remaining, double speed) {
        int ticks = 0;
        while (remaining > 0 && ticks < 100) {
            remaining = MaidItemUseTime.beforeVanillaTick(remaining, speed) - 1;
            ticks++;
        }
        assertTrue("item use must complete", remaining <= 0);
        return ticks;
    }
}
