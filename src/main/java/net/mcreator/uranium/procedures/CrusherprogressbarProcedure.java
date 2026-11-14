package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class CrusherprogressbarProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1) <= 0) {
			return 0;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip3 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip3) : -1) <= 12) {
			return 1;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip5 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip5) : -1) <= 25) {
			return 2;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip7 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip7) : -1) <= 37) {
			return 3;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip9 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip9) : -1) <= 50) {
			return 4;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip11 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip11) : -1) <= 62) {
			return 5;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip13 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip13) : -1) <= 75) {
			return 6;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "crushing") instanceof IntegerProperty _getip15 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip15) : -1) <= 87) {
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