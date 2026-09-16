package com.github.tartaricacid.touhoulittlemaid.client.overlay;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BroomTipsOverlay implements GuiLayer {
    private static final Identifier BG = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");

    @Override
    public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, @NotNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof EntityBroom broom && !broom.hasPassenger(e -> e instanceof EntityMaid)) {
            int screenHeight = GuiGraphicsExtractor.guiHeight();
            int screenWidth = GuiGraphicsExtractor.guiWidth();
            Component tip = Component.translatable("message.touhou_little_maid.broom.unable_fly");
            List<FormattedCharSequence> split = minecraft.font.split(tip, 150);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            GuiGraphicsExtractor.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, BG, screenWidth / 2 - 8, offset - 2, 48, 16, 16, 16, 256, 256);
            offset += 18;
            for (FormattedCharSequence sequence : split) {
                int width = minecraft.font.width(sequence);
                GuiGraphicsExtractor.text(minecraft.font, sequence, (screenWidth - width) / 2, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }
}
