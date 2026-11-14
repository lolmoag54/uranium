package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.Container;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class ReactorprogressbarProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _serverLevel) {
			BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
			if (_be instanceof Container _container) {
				_container.getItem(0).shrink(1);
			}
		}
		if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip2 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip2) : -1) <= 0) {
			return 8;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip4 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip4) : -1) <= 12) {
			return 7;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip6 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip6) : -1) <= 25) {
			return 6;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip8 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip8) : -1) <= 37) {
			return 5;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip10 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip10) : -1) <= 50) {
			return 4;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip12 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip12) : -1) <= 62) {
			return 3;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip14 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip14) : -1) <= 75) {
			return 2;
		} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip16 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip16) : -1) <= 87) {
			return 1;
		}
		return 1;
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