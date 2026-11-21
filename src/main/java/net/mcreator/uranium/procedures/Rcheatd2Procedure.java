package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class Rcheatd2Procedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		double r = 0;
		double t = 0;
		v = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcHeat");
		if (v > 99) {
			v = 99;
		}
		r = v;
		if (r >= 100) {
			r = r - 100;
		}
		t = 0;
		if (r >= 90) {
			r = r - 90;
			t = 9;
		}
		if (r >= 80) {
			r = r - 80;
			t = 8;
		}
		if (r >= 70) {
			r = r - 70;
			t = 7;
		}
		if (r >= 60) {
			r = r - 60;
			t = 6;
		}
		if (r >= 50) {
			r = r - 50;
			t = 5;
		}
		if (r >= 40) {
			r = r - 40;
			t = 4;
		}
		if (r >= 30) {
			r = r - 30;
			t = 3;
		}
		if (r >= 20) {
			r = r - 20;
			t = 2;
		}
		if (r >= 10) {
			r = r - 10;
			t = 1;
		}
		if (r < 1) {
			return 0;
		} else if (r < 2) {
			return 1;
		} else if (r < 3) {
			return 2;
		} else if (r < 4) {
			return 3;
		} else if (r < 5) {
			return 4;
		} else if (r < 6) {
			return 5;
		} else if (r < 7) {
			return 6;
		} else if (r < 8) {
			return 7;
		} else if (r < 9) {
			return 8;
		}
		return 9;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}