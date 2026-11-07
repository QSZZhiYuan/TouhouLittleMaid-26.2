package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.event.AltarRecipesReceivedEvent;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Locale;
import java.util.stream.Collectors;

public class AltarRecipeComponent implements IComponentProcessor {
    private static final String RECIPE_ID = "recipe_id";
    private static final String INPUT = "input";
    private static final String POWER_COST = "power_cost";
    private static final String OUTPUT_ITEM = "output_item";
    private static final String OUTPUT_ENTITY = "output_entity";
    private static final String OUTPUT_DESC = "output_desc";

    private @Nullable AltarRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        Identifier recipeId = Identifier.parse(variables.get(RECIPE_ID, level.registryAccess()).asString());
        this.recipe = null;
        for (RecipeHolder<AltarRecipe> recipe : AltarRecipesReceivedEvent.getRecipes()) {
            if (recipe.id().identifier().equals(recipeId)) {
                this.recipe = recipe.value();
                return;
            }
        }
        TouhouLittleMaid.LOGGER.error("Altar recipe not found: {}", recipeId);
    }

    @Nullable
    @Override
    public IVariable process(Level level, String key) {
        if (recipe == null) {
            return IVariable.from(ItemStack.EMPTY, level.registryAccess());
        }
        if (key.startsWith(INPUT)) {
            int index = Integer.parseInt(key.substring(INPUT.length())) - 1;
            if (index < 0 || index >= recipe.getIngredients().size()) {
                return IVariable.from(ItemStack.EMPTY, level.registryAccess());
            }
            Ingredient ingredient = recipe.getIngredients().get(index);
            String items = ingredient.items().map(item -> BuiltInRegistries.ITEM.getKey(item.value()).toString())
                    .collect(Collectors.joining(","));
            return IVariable.wrap(items, level.registryAccess());
        }

        switch (key) {
            case POWER_COST -> {
                float powerCost = recipe.getPower();
                return IVariable.wrap(String.format(Locale.ROOT, "x%.2f", powerCost), level.registryAccess());
            }
            case OUTPUT_ITEM -> {
                if (!recipe.isItemCraft()) {
                    return IVariable.from(ItemStack.EMPTY, level.registryAccess());
                }
                return IVariable.from(recipe.getResult(), level.registryAccess());
            }
            case OUTPUT_DESC -> {
                return IVariable.wrap(I18n.get(recipe.getLangKey()), level.registryAccess());
            }
            case OUTPUT_ENTITY -> {
                String entityId = recipe.getEntityType().toString();
                if ("touhou_little_maid:box".equals(entityId)) {
                    entityId = "touhou_little_maid:maid";
                }
                return IVariable.wrap(entityId, level.registryAccess());
            }
        }

        return null;
    }
}
