package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class AltarRecipeCategory implements IRecipeCategory<AltarRecipeWrapper> {
    public static final RecipeType<AltarRecipeWrapper> ALTAR = RecipeType.create(TouhouLittleMaid.MOD_ID, "altar", AltarRecipeWrapper.class);
    private static final Identifier ALTAR_ICON = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/altar_icon.png");
    private static final Identifier POWER_ICON = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private static final MutableComponent TITLE = Component.translatable("jei.touhou_little_maid.altar_craft.title");
    private final IDrawableStatic bgDraw;
    private final IDrawable slotDraw;
    private final IDrawableStatic altarDraw;
    private final IDrawableStatic powerDraw;

    public AltarRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createBlankDrawable(160, 125);
        this.slotDraw = guiHelper.getSlotDrawable();
        this.altarDraw = guiHelper.drawableBuilder(ALTAR_ICON, 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.powerDraw = guiHelper.drawableBuilder(POWER_ICON, 32, 0, 16, 16).setTextureSize(64, 64).build();
    }

    @Override
    public void draw(AltarRecipeWrapper recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        int darkGray = 0xFF555555;
        Font font = Minecraft.getInstance().font;
        String result = I18n.get("jei.touhou_little_maid.altar_craft.result", I18n.get(recipe.getLangKey()));

        graphics.pose().pushMatrix();
        graphics.pose().scale(0.8f, 0.8f);
        powerDraw.draw(graphics, 90, 50);
        graphics.pose().popMatrix();

        graphics.text(font, String.format("×%.2f", recipe.getPowerCost()), 65, 55, darkGray, false);
        graphics.text(font, result, (int) ((bgDraw.getWidth() - font.width(result)) / 2.0f), 85, darkGray, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipeWrapper recipe, IFocusGroup focuses) {
        List<Ingredient> inputs = recipe.getInputs();
        ItemStack output = recipe.getOutput();
        int[][] positions = {{40, 35}, {40, 55}, {60, 15}, {80, 15}, {100, 35}, {100, 55}};
        for (int i = 0; i < inputs.size() && i < positions.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, positions[i][0], positions[i][1])
                    .add(inputs.get(i)).setBackground(slotDraw, -1, -1);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 5).addItemStack(output).setBackground(slotDraw, -1, -1);
    }

    @Override
    public RecipeType<AltarRecipeWrapper> getRecipeType() {
        return ALTAR;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public int getWidth() {
        return bgDraw.getWidth();
    }

    @Override
    public int getHeight() {
        return bgDraw.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return altarDraw;
    }
}
