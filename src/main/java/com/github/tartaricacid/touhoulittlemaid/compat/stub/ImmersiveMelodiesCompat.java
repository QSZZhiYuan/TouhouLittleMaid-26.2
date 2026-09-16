package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.CtrlBinding;
import net.minecraft.world.entity.LivingEntity;

public class ImmersiveMelodiesCompat {
    public static final class ImmersiveMelodiesData {
        public float pitch = 0;
        public float volume = 0;
        public float current = 0;
        public long delta = 0;
        public long time = 0;
    }

    public static void init() {
    }

    public static void updateMelodyProgress(LivingEntity entity, ImmersiveMelodiesData imData) {
    }

    public static void addBinding(CtrlBinding binding) {
        binding.livingEntityVar("im_pitch", ctx -> 0f);
        binding.livingEntityVar("im_volume", ctx -> 0f);
        binding.livingEntityVar("im_current", ctx -> 0f);
        binding.livingEntityVar("im_delta", ctx -> 0L);
        binding.livingEntityVar("im_time", ctx -> 0L);
    }

    public static void addAnimation(HardcodedAnimationManger manger) {
    }
}