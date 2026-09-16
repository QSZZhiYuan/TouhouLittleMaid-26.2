package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockAltar;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.block.BlockStatue;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class ClientExtensionsEvent {
    @SubscribeEvent
    public static void RegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerBlock(BlockAltar.CLIENT_BLOCK_EXTENSIONS, InitBlocks.ALTAR.get());
        event.registerBlock(BlockGarageKit.CLIENT_BLOCK_EXTENSIONS, InitBlocks.GARAGE_KIT.get());
        event.registerBlock(BlockStatue.CLIENT_BLOCK_EXTENSIONS, InitBlocks.STATUE.get());
        event.registerItem(IClientItemExtensions.DEFAULT, InitItems.CHAIR.get());
        event.registerItem(IClientItemExtensions.DEFAULT, InitItems.ENTITY_PLACEHOLDER.get());
        event.registerItem(IClientItemExtensions.DEFAULT, InitItems.GARAGE_KIT.get());
        event.registerItem(IClientItemExtensions.DEFAULT, InitItems.PICNIC_BASKET.get());
    }
}
