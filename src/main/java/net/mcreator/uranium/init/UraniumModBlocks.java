/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import net.mcreator.uranium.block.*;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

public class UraniumModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(UraniumMod.MODID);
	public static final DeferredBlock<Block> CENTRIFUGE;
	public static final DeferredBlock<Block> SULFURICACID;
	public static final DeferredBlock<Block> RAWURANIUM;
	public static final DeferredBlock<Block> BLOCKDEPLOMB;
	public static final DeferredBlock<Block> MINERAIDEPLOMB;
	public static final DeferredBlock<Block> GROSSEBATTERIE;
	public static final DeferredBlock<Block> SOLARPANEL;
	public static final DeferredBlock<Block> CABLE;
	public static final DeferredBlock<Block> CABLE_E;
	public static final DeferredBlock<Block> CABLE_I;
	public static final DeferredBlock<Block> CABLE_L;
	public static final DeferredBlock<Block> CABLE_LC;
	public static final DeferredBlock<Block> CABLE_LCC;
	public static final DeferredBlock<Block> CABLE_LT;
	public static final DeferredBlock<Block> CABLE_LTC;
	public static final DeferredBlock<Block> CABLE_T;
	public static final DeferredBlock<Block> CABLE_TC;
	public static final DeferredBlock<Block> CABLE_TX;
	public static final DeferredBlock<Block> CABLE_TXC;
	public static final DeferredBlock<Block> CABLE_TXCC;
	public static final DeferredBlock<Block> CABLE_X;
	public static final DeferredBlock<Block> CABLE_XC;
	public static final DeferredBlock<Block> CABLE_F;
	public static final DeferredBlock<Block> CABLE_S;
	public static final DeferredBlock<Block> RAWURANIUM_DEEPSLATE;
	static {
		CENTRIFUGE = register("centrifuge", CentrifugeBlock::new);
		SULFURICACID = register("sulfuricacid", SulfuricacidBlock::new);
		RAWURANIUM = register("rawuranium", RawuraniumBlock::new);
		BLOCKDEPLOMB = register("blockdeplomb", BlockdeplombBlock::new);
		MINERAIDEPLOMB = register("mineraideplomb", MineraideplombBlock::new);
		GROSSEBATTERIE = register("grossebatterie", GrossebatterieBlock::new);
		SOLARPANEL = register("solarpanel", SolarpanelBlock::new);
		CABLE = register("cable", CableBlock::new);
		CABLE_E = register("cable_e", CableEBlock::new);
		CABLE_I = register("cable_i", CableIBlock::new);
		CABLE_L = register("cable_l", CableLBlock::new);
		CABLE_LC = register("cable_lc", CableLCBlock::new);
		CABLE_LCC = register("cable_lcc", CableLCCBlock::new);
		CABLE_LT = register("cable_lt", CableLTBlock::new);
		CABLE_LTC = register("cable_ltc", CableLTCBlock::new);
		CABLE_T = register("cable_t", CableTBlock::new);
		CABLE_TC = register("cable_tc", CableTCBlock::new);
		CABLE_TX = register("cable_tx", CableTXBlock::new);
		CABLE_TXC = register("cable_txc", CableTXCBlock::new);
		CABLE_TXCC = register("cable_txcc", CableTXCCBlock::new);
		CABLE_X = register("cable_x", CableXBlock::new);
		CABLE_XC = register("cable_xc", CableXCBlock::new);
		CABLE_F = register("cable_f", CableFBlock::new);
		CABLE_S = register("cable_s", CableSBlock::new);
		RAWURANIUM_DEEPSLATE = register("rawuranium_deepslate", RawuraniumDeepslateBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}