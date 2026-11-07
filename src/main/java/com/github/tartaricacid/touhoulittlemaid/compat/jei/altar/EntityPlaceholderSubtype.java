package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("removal")
public class EntityPlaceholderSubtype implements mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter<ItemStack> {
    @Override
    public Object getSubtypeData(ItemStack itemStack, UidContext context) {
        Identifier recipeId = ItemEntityPlaceholder.getId(itemStack);
        if (recipeId == null) {
            return null;
        }
        return recipeId.toString();
    }
}
