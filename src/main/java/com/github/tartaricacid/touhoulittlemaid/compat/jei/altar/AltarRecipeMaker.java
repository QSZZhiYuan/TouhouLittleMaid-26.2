package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.client.event.AltarRecipesReceivedEvent;
import com.github.tartaricacid.touhoulittlemaid.util.JERIUtil;

import java.util.ArrayList;
import java.util.List;

public final class AltarRecipeMaker {
    private AltarRecipeMaker() {
    }

    public static AltarRecipeMaker getInstance() {
        return new AltarRecipeMaker();
    }

    public List<AltarRecipeWrapper> getAltarRecipes() {
        List<AltarRecipeWrapper> recipes = new ArrayList<>();
        JERIUtil.recipeWarpHolder(AltarRecipesReceivedEvent.getRecipes(), (recipeId, inputs, output, powerCost, langKey) ->
                recipes.add(new AltarRecipeWrapper(List.copyOf(inputs), output, powerCost, langKey)));
        return recipes;
    }
}
