package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcbarheatProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcHeat");
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

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}