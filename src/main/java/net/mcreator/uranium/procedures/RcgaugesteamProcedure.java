package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcgaugesteamProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = 0;
		if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1) > 0
				&& !getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcScram")) {
			v = ((15 - getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcDepth")) * 80) / 15;
		}
		if (v < 2) {
			return 0;
		} else if (v < 4) {
			return 1;
		} else if (v < 6) {
			return 2;
		} else if (v < 8) {
			return 3;
		} else if (v < 10) {
			return 4;
		} else if (v < 12) {
			return 5;
		} else if (v < 14) {
			return 6;
		} else if (v < 16) {
			return 7;
		} else if (v < 18) {
			return 8;
		} else if (v < 20) {
			return 9;
		} else if (v < 22) {
			return 10;
		} else if (v < 24) {
			return 11;
		} else if (v < 26) {
			return 12;
		} else if (v < 28) {
			return 13;
		} else if (v < 30) {
			return 14;
		} else if (v < 32) {
			return 15;
		} else if (v < 34) {
			return 16;
		} else if (v < 36) {
			return 17;
		} else if (v < 38) {
			return 18;
		} else if (v < 40) {
			return 19;
		} else if (v < 42) {
			return 20;
		} else if (v < 44) {
			return 21;
		} else if (v < 46) {
			return 22;
		} else if (v < 48) {
			return 23;
		} else if (v < 50) {
			return 24;
		} else if (v < 52) {
			return 25;
		} else if (v < 54) {
			return 26;
		} else if (v < 56) {
			return 27;
		} else if (v < 58) {
			return 28;
		} else if (v < 60) {
			return 29;
		} else if (v < 62) {
			return 30;
		} else if (v < 64) {
			return 31;
		} else if (v < 66) {
			return 32;
		} else if (v < 68) {
			return 33;
		} else if (v < 70) {
			return 34;
		} else if (v < 72) {
			return 35;
		} else if (v < 74) {
			return 36;
		} else if (v < 76) {
			return 37;
		} else if (v < 78) {
			return 38;
		} else if (v < 80) {
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

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}