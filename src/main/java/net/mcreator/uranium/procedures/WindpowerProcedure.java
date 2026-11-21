package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class WindpowerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double power = 0;
		if ((world instanceof Level _lvl0 && _lvl0.isRaining() || world instanceof Level _lvl1 && _lvl1.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			power = 50;
		} else if (!(world instanceof Level _lvl3 && _lvl3.isRaining() || world instanceof Level _lvl4 && _lvl4.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))
				|| (world instanceof Level _lvl6 && _lvl6.isRaining() || world instanceof Level _lvl7 && _lvl7.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			power = 25;
		} else if (!(world instanceof Level _lvl9 && _lvl9.isRaining() || world instanceof Level _lvl10 && _lvl10.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			power = 10;
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.UP) && getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), "cablesFCopperUp")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y - 1, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH) && getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), "cablesFCopperSouth")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z - 1);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x + 1, y, z), Direction.WEST) && getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), "cablesFCopperWest")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x + 1, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), Direction.WEST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z + 1), Direction.NORTH) && getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), "cablesFCopperNorth")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z + 1);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x - 1, y, z), Direction.EAST) && getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), "cablesFCopperEast")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x - 1, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), Direction.EAST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y + 1, z), Direction.DOWN) && getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), "cablesFCopperDown")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y + 1, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y + 1, z), Direction.DOWN) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) power, _tx);
						_tx.commit();
					}
				}
			}
		}
	}

	private static boolean canReceiveEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getCapacityAsLong() > 0;
		}
		return false;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}
}