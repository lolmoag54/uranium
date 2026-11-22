package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcgaugefuelProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1;
		if (v < 2.5) {
			return 0;
		} else if (v < 5) {
			return 1;
		} else if (v < 7.5) {
			return 2;
		} else if (v < 10) {
			return 3;
		} else if (v < 12.5) {
			return 4;
		} else if (v < 15) {
			return 5;
		} else if (v < 17.5) {
			return 6;
		} else if (v < 20) {
			return 7;
		} else if (v < 22.5) {
			return 8;
		} else if (v < 25) {
			return 9;
		} else if (v < 27.5) {
			return 10;
		} else if (v < 30) {
			return 11;
		} else if (v < 32.5) {
			return 12;
		} else if (v < 35) {
			return 13;
		} else if (v < 37.5) {
			return 14;
		} else if (v < 40) {
			return 15;
		} else if (v < 42.5) {
			return 16;
		} else if (v < 45) {
			return 17;
		} else if (v < 47.5) {
			return 18;
		} else if (v < 50) {
			return 19;
		} else if (v < 52.5) {
			return 20;
		} else if (v < 55) {
			return 21;
		} else if (v < 57.5) {
			return 22;
		} else if (v < 60) {
			return 23;
		} else if (v < 62.5) {
			return 24;
		} else if (v < 65) {
			return 25;
		} else if (v < 67.5) {
			return 26;
		} else if (v < 70) {
			return 27;
		} else if (v < 72.5) {
			return 28;
		} else if (v < 75) {
			return 29;
		} else if (v < 77.5) {
			return 30;
		} else if (v < 80) {
			return 31;
		} else if (v < 82.5) {
			return 32;
		} else if (v < 85) {
			return 33;
		} else if (v < 87.5) {
			return 34;
		} else if (v < 90) {
			return 35;
		} else if (v < 92.5) {
			return 36;
		} else if (v < 95) {
			return 37;
		} else if (v < 97.5) {
			return 38;
		} else if (v < 100) {
			return 39;
		}
		return 40;
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