/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.uranium.item.*;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

@EventBusSubscriber
public class UraniumModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(UraniumMod.MODID);
	public static final DeferredItem<Item> STEEL;
	public static final DeferredItem<Item> CENTRIFUGE;
	public static final DeferredItem<Item> DEPLETEDURANIUM;
	public static final DeferredItem<Item> ENRICHEDURANIUM;
	public static final DeferredItem<Item> YELLOWCAKE;
	public static final DeferredItem<Item> SULFURICACID_BUCKET;
	public static final DeferredItem<Item> RAWURANIUM;
	public static final DeferredItem<Item> FLUORIDE;
	public static final DeferredItem<Item> LINGOTDEPLOMB;
	public static final DeferredItem<Item> PEPITEDEPLOMB;
	public static final DeferredItem<Item> BLOCKDEPLOMB;
	public static final DeferredItem<Item> MINERAIDEPLOMB;
	static {
		STEEL = register("steel", SteelItem::new);
		CENTRIFUGE = block(UraniumModBlocks.CENTRIFUGE, new Item.Properties().stacksTo(16));
		DEPLETEDURANIUM = register("depleteduranium", DepleteduraniumItem::new);
		ENRICHEDURANIUM = register("enricheduranium", EnricheduraniumItem::new);
		YELLOWCAKE = register("yellowcake", YellowcakeItem::new);
		SULFURICACID_BUCKET = register("sulfuricacid_bucket", SulfuricacidItem::new);
		RAWURANIUM = block(UraniumModBlocks.RAWURANIUM);
		FLUORIDE = register("fluoride", FluorideItem::new);
		LINGOTDEPLOMB = register("lingotdeplomb", LingotdeplombItem::new);
		PEPITEDEPLOMB = register("pepitedeplomb", PepitedeplombItem::new);
		BLOCKDEPLOMB = block(UraniumModBlocks.BLOCKDEPLOMB);
		MINERAIDEPLOMB = block(UraniumModBlocks.MINERAIDEPLOMB);
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

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new BucketResourceHandler(access), SULFURICACID_BUCKET.get());
	}
}