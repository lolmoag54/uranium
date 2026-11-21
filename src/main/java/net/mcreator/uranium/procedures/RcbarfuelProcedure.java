package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcbarfuelProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1;
		if (v < 5) {
			return 0;
		} else if (v < 10) {
			return 1;
		} else if (v < 15) {
			return 2;
		} else if (v < 20) {
			return 3;
		} else if (v < 25) {
			return 4;
		} else if (v < 30) {
			return 5;
		} else if (v < 35) {
			return 6;
		} else if (v < 40) {
			return 7;
		} else if (v < 45) {
			return 8;
		} else if (v < 50) {
			return 9;
		} else if (v < 55) {
			return 10;
		} else if (v < 60) {
			return 11;
		} else if (v < 65) {
			return 12;
		} else if (v < 70) {
			return 13;
		} else if (v < 75) {
			return 14;
		} else if (v < 80) {
			return 15;
		} else if (v < 85) {
			return 16;
		} else if (v < 90) {
			return 17;
		} else if (v < 95) {
			return 18;
		} else if (v < 100) {
			return 19;
		}
		return 20;
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