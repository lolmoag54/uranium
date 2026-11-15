package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class CrusherpowerbarProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 0) {
			return 0;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 2500) {
			return 1;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 5000) {
			return 2;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 7500) {
			return 3;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 10000) {
			return 4;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 12500) {
			return 5;
		} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 15000) {
			return 6;
		}
		return 7;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getAmountAsInt();
		}
		return 0;
	}
}