package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcxtalprogressProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crystallizing") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1;
		if (v < 15) {
			return 0;
		} else if (v < 30) {
			return 1;
		} else if (v < 45) {
			return 2;
		} else if (v < 60) {
			return 3;
		} else if (v < 75) {
			return 4;
		} else if (v < 90) {
			return 5;
		} else if (v < 105) {
			return 6;
		} else if (v < 120) {
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