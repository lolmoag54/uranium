package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class RcreppowerProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getEnergyStored(world, BlockPos.containing(x, y, z), null);
		if (v < 1000) {
			return 0;
		} else if (v < 2000) {
			return 1;
		} else if (v < 3000) {
			return 2;
		} else if (v < 4000) {
			return 3;
		} else if (v < 5000) {
			return 4;
		} else if (v < 6000) {
			return 5;
		} else if (v < 7000) {
			return 6;
		} else if (v < 8000) {
			return 7;
		} else if (v < 9000) {
			return 8;
		} else if (v < 10000) {
			return 9;
		} else if (v < 11000) {
			return 10;
		} else if (v < 12000) {
			return 11;
		} else if (v < 13000) {
			return 12;
		} else if (v < 14000) {
			return 13;
		} else if (v < 15000) {
			return 14;
		} else if (v < 16000) {
			return 15;
		} else if (v < 17000) {
			return 16;
		} else if (v < 18000) {
			return 17;
		} else if (v < 19000) {
			return 18;
		} else if (v < 20000) {
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