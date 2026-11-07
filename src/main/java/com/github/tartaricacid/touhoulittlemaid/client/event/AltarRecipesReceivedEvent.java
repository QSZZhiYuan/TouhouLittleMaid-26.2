package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class AltarRecipesReceivedEvent {
    private static List<RecipeHolder<AltarRecipe>> recipes = List.of();

    public static List<RecipeHolder<AltarRecipe>> getRecipes() {
        return recipes;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        recipes = List.copyOf(event.getRecipeMap().byType(InitRecipes.ALTAR_CRAFTING.get()));
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        recipes = List.of();
    }
}
