package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class RcpipetickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double a = 0;
		double a0 = 0;
		double b = 0;
		double cp = 0;
		double m = 0;
		if (!world.isClientSide()) {
			if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcInit")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("rcInit", true);
						_blockEntity.getPersistentData().putBoolean("rcFluid", true);
						_blockEntity.getPersistentData().putBoolean("rcSink", false);
						_blockEntity.getPersistentData().putDouble("rcCap", 500);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			a = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater");
			a0 = a;
			if (getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x + 1, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x + 1, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x - 1, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x - 1, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y + 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y + 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z + 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z + 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), "rcFluid")) {
				if (getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), "rcSink")) {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "rcWater");
					cp = getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "rcCap");
					m = 40;
					if (a < m) {
						m = a;
					}
					if (cp - b < m) {
						m = cp - b;
					}
					if (m > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z - 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				} else {
					b = getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "rcWater");
					if (a > b + 4) {
						m = (a - b) / 2;
						if (m > 40) {
							m = 40;
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z - 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("rcWater", (b + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						a = a - m;
					}
				}
			}
			if (!(a == a0)) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcWater", a);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
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