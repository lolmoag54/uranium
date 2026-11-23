package net.mcreator.uranium.init;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.List;
import java.util.ArrayList;

@JeiPlugin
public class UraniumModBrewingRecipes implements IModPlugin {
	@Override
	public Identifier getPluginUid() {
		return Identifier.parse("uranium:brewing_recipes");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
		List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
		ItemStack potion = new ItemStack(Items.POTION);
		ItemStack potion2 = new ItemStack(Items.POTION);
		List<ItemStack> ingredientStack = new ArrayList<>();
		List<ItemStack> inputStack = new ArrayList<>();
		ingredientStack.add(new ItemStack(UraniumModItems.FLUORIDE.get()));
		inputStack.add(new ItemStack(UraniumModItems.EMPTYVIAL.get()));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), List.copyOf(inputStack), new ItemStack(UraniumModItems.FLUOR.get()), Identifier.parse("uranium:fluorrecipe")));
		inputStack.clear();
		ingredientStack.clear();
		ingredientStack.add(new ItemStack(UraniumModItems.FLUOR.get()));
		inputStack.add(new ItemStack(UraniumModItems.YELLOWCAKE.get()));
		brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), List.copyOf(inputStack), new ItemStack(UraniumModItems.URANIUMHEXAFLUORIDE.get()), Identifier.parse("uranium:uraniumhexafluoride_recipe")));
		inputStack.clear();
		ingredientStack.clear();
		registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
	}
}