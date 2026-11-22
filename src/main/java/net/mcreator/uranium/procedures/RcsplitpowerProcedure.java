package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class RcsplitpowerProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getEnergyStored(world, BlockPos.containing(x, y, z), null);
		if (v < 20000) {
			return 0;
		} else if (v < 40000) {
			return 1;
		} else if (v < 60000) {
			return 2;
		} else if (v < 80000) {
			return 3;
		} else if (v < 100000) {
			return 4;
		} else if (v < 120000) {
			return 5;
		} else if (v < 140000) {
			return 6;
		} else if (v < 160000) {
			return 7;
		} else if (v < 180000) {
			return 8;
		} else if (v < 200000) {
			return 9;
		} else if (v < 220000) {
			return 10;
		} else if (v < 240000) {
			return 11;
		} else if (v < 260000) {
			return 12;
		} else if (v < 280000) {
			return 13;
		} else if (v < 300000) {
			return 14;
		} else if (v < 320000) {
			return 15;
		} else if (v < 340000) {
			return 16;
		} else if (v < 360000) {
			return 17;
		} else if (v < 380000) {
			return 18;
		} else if (v < 400000) {
			return 19;
		}
		return 20;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getAmountAsInt();
		}
		return 0;
	}
}