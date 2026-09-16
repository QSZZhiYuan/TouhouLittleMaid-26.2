package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.google.common.collect.Maps;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

public final class InnerAnimation {
    static final HashMap<Identifier, IAnimation<?>> INNER_ANIMATION = Maps.newHashMap();

    public static boolean containsKey(Identifier Identifier) {
        return INNER_ANIMATION.containsKey(Identifier);
    }

    public static IAnimation<?> get(Identifier Identifier) {
        return INNER_ANIMATION.get(Identifier);
    }

    public static void init() {
        INNER_ANIMATION.clear();
        MaidBaseAnimation.init();
        MaidExtraAnimation.init();
        MaidArmorAnimation.init();
        MaidTaskAnimation.init();
        ChairBaseAnimation.init();
        EntityBaseAnimation.init();
        PlayerMaidAnimation.init();
        SpecialAnimation.init();
        FestivalAnimation.init();
    }
}
