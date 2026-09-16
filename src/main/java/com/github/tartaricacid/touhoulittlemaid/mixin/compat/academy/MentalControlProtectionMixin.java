package com.github.tartaricacid.touhoulittlemaid.mixin.compat.academy;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "org.academy.internal.common.ability.mentalout.control.MentalControlProtection", remap = false, priority = 500)
public class MentalControlProtectionMixin {
    @Inject(method = "notifyBlocked", at = @At("HEAD"), cancellable = true, remap = false)
    private static void touhouLittleMaid$onNotifyBlocked(ServerPlayer controller, LivingEntity subject, CallbackInfo ci) {
        if (!ci.isCancelled() && subject instanceof EntityMaid) {
            controller.sendOverlayMessage(Component.translatable("message.touhou_little_maid.mental_control_immune"));
            ci.cancel();
        }
    }
}
