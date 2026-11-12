package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class GrossebatterieOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double north = 0;
		double south = 0;
		double east = 0;
		double west = 0;
		double down = 0;
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.UP)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.UP);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.NORTH)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.NORTH);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.NORTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.SOUTH)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.SOUTH);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.SOUTH) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.WEST)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.WEST);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.WEST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.EAST)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.EAST);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.EAST) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
						_tx.commit();
					}
				}
			}
		}
		if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.DOWN)) {
			down = extractEnergySimulate(world, BlockPos.containing(x, y, z), 10000, null);
			down = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) down, Direction.DOWN);
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract((int) down, _tx);
						_tx.commit();
					}
				}
			}
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.DOWN) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert((int) down, _tx);
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

	private static int extractEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler) {
				try (var tx = Transaction.openRoot()) {
					return energyHandler.extract(amount, tx);
				}
			}
		}
		return 0;
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