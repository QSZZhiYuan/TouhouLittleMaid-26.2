package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.CraftingTableBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingTableBackpack extends IMaidBackpack {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "crafting_table_backpack");

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, Player player, EntityMaid maid) {
        dropRelativeItems(stack, maid);
    }

    @Override
    public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
    }

    @Override
    public MenuProvider getGuiProvider(int entityId) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Crafting Table Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new CraftingTableBackpackContainer(index, playerInventory, entityId);
            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
            }
        };
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.CRAFTING_TABLE_CAPACITY;
    }

    @Override

    public Identifier getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return InitItems.CRAFTING_TABLE_BACKPACK.get();
    }
}
