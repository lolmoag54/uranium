package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcrepprogressProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "reprocessing") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1;
		if (v < 12.5) {
			return 0;
		} else if (v < 25) {
			return 1;
		} else if (v < 37.5) {
			return 2;
		} else if (v < 50) {
			return 3;
		} else if (v < 62.5) {
			return 4;
		} else if (v < 75) {
			return 5;
		} else if (v < 87.5) {
			return 6;
		} else if (v < 100) {
			return 7;
		}
		return 8;
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}