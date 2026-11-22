package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class Rcfueld2Procedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double v = 0;
		double r = 0;
		double t = 0;
		v = getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip1 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip1) : -1;
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
		if (v < 10) {
			return 10;
		}
		return t;
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