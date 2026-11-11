/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.uranium.item.YellowcakeItem;
import net.mcreator.uranium.item.SteelItem;
import net.mcreator.uranium.item.EnricheduraniumItem;
import net.mcreator.uranium.item.DepleteduraniumItem;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

public class UraniumModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(UraniumMod.MODID);
	public static final DeferredItem<Item> STEEL;
	public static final DeferredItem<Item> CENTRIFUGE;
	public static final DeferredItem<Item> DEPLETEDURANIUM;
	public static final DeferredItem<Item> ENRICHEDURANIUM;
	public static final DeferredItem<Item> YELLOWCAKE;
	static {
		STEEL = register("steel", SteelItem::new);
		CENTRIFUGE = block(UraniumModBlocks.CENTRIFUGE, new Item.Properties().stacksTo(16));
		DEPLETEDURANIUM = register("depleteduranium", DepleteduraniumItem::new);
		ENRICHEDURANIUM = register("enricheduranium", EnricheduraniumItem::new);
		YELLOWCAKE = register("yellowcake", YellowcakeItem::new);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, Item.Properties::new);
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), () -> properties);
	}
}