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

public class ReactorenergyoutputProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double send = 0;
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
			send = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), 64, Direction.UP);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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
			send = receiveEnergySimulate(world, BlockPos.containing(x, y + 1, z), 64, Direction.DOWN);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y + 1, z), Direction.DOWN) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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
			send = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), 64, Direction.SOUTH);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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
			send = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), 64, Direction.NORTH);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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
			send = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), 64, Direction.WEST);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), Direction.WEST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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
			send = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), 64, Direction.EAST);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), Direction.EAST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) send, _tx);
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

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler) {
				try (var tx = Transaction.openRoot()) {
					return energyHandler.insert(amount, tx);
				}
			}
		}
		return 0;
	}
}