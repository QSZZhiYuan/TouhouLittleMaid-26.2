package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.other.PicnicBasketContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class PicnicBasketContainerScreen extends AbstractContainerScreen<PicnicBasketContainer> {
    private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    public PicnicBasketContainerScreen(PicnicBasketContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        this.renderBg(graphics, partialTick, mouseX, mouseY);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
    }

    protected void renderBg(GuiGraphicsExtractor GuiGraphicsExtractor, float partialTick, int mouseX, int mouseY) {
        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        GuiGraphicsExtractor.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, middleX, middleY, 0, 0, this.imageWidth, 35, 256, 256);
        GuiGraphicsExtractor.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, middleX, middleY + 35, 0, 126, this.imageWidth, 96, 256, 256);
    }
}
