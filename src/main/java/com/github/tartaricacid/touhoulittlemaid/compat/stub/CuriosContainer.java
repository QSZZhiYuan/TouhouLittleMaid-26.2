package com.github.tartaricacid.touhoulittlemaid.compat.stub;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class CuriosContainer extends AbstractContainerMenu {
    public static final MenuType<CuriosContainer> TYPE = null;

    public CuriosContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id);
    }

    public static MenuProvider create(EntityMaid maid) {
        return null;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
