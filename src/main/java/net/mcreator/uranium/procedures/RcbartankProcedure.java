package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class RcbartankProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		v = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcTank");
		if (v < 800) {
			return 0;
		} else if (v < 1600) {
			return 1;
		} else if (v < 2400) {
			return 2;
		} else if (v < 3200) {
			return 3;
		} else if (v < 4000) {
			return 4;
		} else if (v < 4800) {
			return 5;
		} else if (v < 5600) {
			return 6;
		} else if (v < 6400) {
			return 7;
		} else if (v < 7200) {
			return 8;
		} else if (v < 8000) {
			return 9;
		} else if (v < 8800) {
			return 10;
		} else if (v < 9600) {
			return 11;
		} else if (v < 10400) {
			return 12;
		} else if (v < 11200) {
			return 13;
		} else if (v < 12000) {
			return 14;
		} else if (v < 12800) {
			return 15;
		} else if (v < 13600) {
			return 16;
		} else if (v < 14400) {
			return 17;
		} else if (v < 15200) {
			return 18;
		} else if (v < 16000) {
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