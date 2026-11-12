/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import net.mcreator.uranium.block.SulfuricacidBlock;
import net.mcreator.uranium.block.RawuraniumBlock;
import net.mcreator.uranium.block.CentrifugeBlock;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

public class UraniumModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(UraniumMod.MODID);
	public static final DeferredBlock<Block> CENTRIFUGE;
	public static final DeferredBlock<Block> SULFURICACID;
	public static final DeferredBlock<Block> RAWURANIUM;
	static {
		CENTRIFUGE = register("centrifuge", CentrifugeBlock::new);
		SULFURICACID = register("sulfuricacid", SulfuricacidBlock::new);
		RAWURANIUM = register("rawuranium", RawuraniumBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}