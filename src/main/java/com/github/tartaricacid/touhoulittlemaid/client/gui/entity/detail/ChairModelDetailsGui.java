package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ModelDetailsButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;

public class ChairModelDetailsGui extends AbstractModelDetailsGui<EntityChair, ChairModelInfo> {
    private EntityMaid maid;
    private boolean showPassenger = false;

    public ChairModelDetailsGui(EntityChair sourceEntity, ChairModelInfo modelInfo) {
        super(sourceEntity, InitEntities.CHAIR.get().create(sourceEntity.level(), net.minecraft.world.entity.EntitySpawnReason.NATURAL), modelInfo);
        guiEntity.setModelId(modelInfo.getModelId().toString());
        if (Minecraft.getInstance().level != null) {
            this.maid = InitEntities.MAID.get().create(Minecraft.getInstance().level, net.minecraft.world.entity.EntitySpawnReason.NATURAL);
            if (this.maid != null) {
                this.maid.setModelId("authors_and_credits:wine_fox_maid");
            }
        }
    }

    @Override
    protected void applyReturnButtonLogic() {
        CacheIconManager.openChairModelGui(sourceEntity);
    }

    @Override
    protected void initSideButton() {
        ModelDetailsButton showPassengerButton = new ModelDetailsButton(2, 17, "gui.touhou_little_maid.skin_details.show_passenger",
                this::applyShowPassengerLogic);
        this.addRenderableWidget(showPassengerButton);
    }

    @Override
    public void tick() {
        guiEntity.tickCount++;
        if (maid != null) {
            maid.tickCount++;
        }
    }

    @Override
    protected void renderExtraEntity(EntityRenderDispatcher manager, PoseStack matrix, SubmitNodeCollector bufferIn) {
        // Extra entity rendering is handled by InventoryScreen.renderEntityInInventoryFollowsAngle
    }

    private void applyShowPassengerLogic(boolean isStateTriggered) {
        this.showPassenger = isStateTriggered;
        if (isStateTriggered && maid != null) {
            maid.startRiding(guiEntity, true, true);
        } else {
            guiEntity.ejectPassengers();
        }
    }
}
