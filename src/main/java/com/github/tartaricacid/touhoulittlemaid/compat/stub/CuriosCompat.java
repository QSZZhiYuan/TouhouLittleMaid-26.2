package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class CuriosCompat {
    public static void init() {
    }

    public static boolean isLoaded() {
        return false;
    }

    public static boolean isLoadedOrEnable() {
        return false;
    }

    public static MenuProvider create(EntityMaid maid) {
        return null;
    }

    public static void registerScreen(RegisterMenuScreensEvent event) {
    }

    public static void clientUpdatePage(int page) {
    }

    public static void clientResetPage() {
    }
}