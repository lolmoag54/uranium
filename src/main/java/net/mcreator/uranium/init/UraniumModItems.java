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

import net.mcreator.uranium.item.SteelItem;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

public class UraniumModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(UraniumMod.MODID);
	public static final DeferredItem<Item> STEEL;
	public static final DeferredItem<Item> CENTRIFUGE;
	static {
		STEEL = register("steel", SteelItem::new);
		CENTRIFUGE = block(UraniumModBlocks.CENTRIFUGE, new Item.Properties().stacksTo(16));
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