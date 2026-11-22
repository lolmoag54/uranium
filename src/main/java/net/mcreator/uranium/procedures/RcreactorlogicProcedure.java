package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class RcreactorlogicProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double p = 0;
		double h = 0;
		double tk = 0;
		double vn = 0;
		double c = 0;
		if (!world.isClientSide()) {
			if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcInit2")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("rcInit2", true);
						_blockEntity.getPersistentData().putBoolean("rcFluid", true);
						_blockEntity.getPersistentData().putBoolean("rcSink", true);
						_blockEntity.getPersistentData().putDouble("rcCap", 8000);
						_blockEntity.getPersistentData().putDouble("rcDepth", 8);
						_blockEntity.getPersistentData().putDouble("rcVent", 0);
						_blockEntity.getPersistentData().putDouble("rcHeat", 0);
						_blockEntity.getPersistentData().putDouble("rcTank", 0);
						_blockEntity.getPersistentData().putDouble("rcWater", 0);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			RcwaterintakeProcedure.execute(world, x, y, z);
			p = 0;
			if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "burn") instanceof IntegerProperty _getip12 ? (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getip12) : -1) > 0
					&& !getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcScram")) {
				p = (15 - getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcDepth")) / 15;
			}
			h = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcHeat");
			tk = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcTank");
			vn = 0;
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcVent") == 1 || getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcVent") == 0 && tk >= 14000) {
				vn = 1;
			}
			tk = tk + p * 80;
			if (vn == 1) {
				tk = tk - 60;
			}
			if (tk > 16000) {
				tk = 16000;
			}
			if (tk < 0) {
				tk = 0;
			}
			c = 0.05 + h * 0.003;
			if (vn == 1) {
				c = c + 0.04;
			}
			if (tk >= 16000 && vn == 0) {
				c = 0.02;
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater") > 0 && h > 50) {
				c = c + 0.15;
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcWater", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater") - 2));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater") < 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcWater", 0);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			h = (h + p * (0.3 + getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcMox") * 0.1)) - c;
			if (h < 0) {
				h = 0;
			}
			if (h > 100) {
				h = 100;
			}
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("rcHeat", h);
					_blockEntity.getPersistentData().putDouble("rcTank", tk);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcScram")) {
				{
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
						world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
				}
			}
			if (h >= 100) {
				RcmeltdownProcedure.execute(world, x, y, z);
			}
		}
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}