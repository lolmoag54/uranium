package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModBlocks;

public class CableModelUpdaterMainProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE.get())) {
				CableModelUpdaterNProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_E.get())) {
				CableModelUpdaterEProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_I.get())) {
				CableModelUpdaterIProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_L.get())) {
				CableModelUpdaterLProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_LC.get())) {
				CableModelUpdaterLCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_LCC.get())) {
				CableModelUpdaterLCCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_LT.get())) {
				CableModelUpdaterLTProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_LTC.get())) {
				CableModelUpdaterLTCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_T.get())) {
				CableModelUpdaterTProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_TC.get())) {
				CableModelUpdaterTCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_TX.get())) {
				CableModelUpdaterTXProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_TXC.get())) {
				CableModelUpdaterTXCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_TXCC.get())) {
				CableModelUpdaterTXCCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_X.get())) {
				CableModelUpdaterXProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_XC.get())) {
				CableModelUpdaterXCProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_F.get())) {
				CableModelUpdaterFProcedure.execute(world, x, y, z);
			}
			if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UraniumModBlocks.CABLE_S.get())) {
				CableModelUpdaterSProcedure.execute(world, x, y, z);
			}
		}
	}
}