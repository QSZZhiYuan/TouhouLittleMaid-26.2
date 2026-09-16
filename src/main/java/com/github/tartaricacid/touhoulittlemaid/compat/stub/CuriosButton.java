package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

public class CuriosButton extends AbstractButton {
    public CuriosButton(int x, int y, boolean isOpen, Button.OnPress onPress) {
        super(x, y, 14, 14, Component.empty());
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void onPress(InputWithModifiers input) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
