package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class SolarpanelOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double testSendDown = 0;
		double testSendNorth = 0;
		double testSendSouth = 0;
		double testSendEast = 0;
		double testSendWest = 0;
		if (getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), "cablesFCopperUp") && world instanceof Level _lvl1 && _lvl1.isBrightOutside() && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y, z))) {
			if (world instanceof Level _lvl3 && _lvl3.isThundering() && world instanceof Level _lvl4 && _lvl4.isRaining()) {
				testSendDown = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), 10, null);
				testSendNorth = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), 10, null);
				testSendSouth = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), 10, null);
				testSendEast = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), 10, null);
				testSendWest = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), 10, null);
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendDown, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendNorth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendSouth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendEast, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendWest, _tx);
							_tx.commit();
						}
					}
				}
			} else if (!(world instanceof Level _lvl15 && _lvl15.isThundering()) && world instanceof Level _lvl16 && _lvl16.isRaining()
					|| world instanceof Level _lvl17 && _lvl17.isThundering() && !(world instanceof Level _lvl18 && _lvl18.isRaining())) {
				testSendDown = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), 25, null);
				testSendNorth = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), 25, null);
				testSendSouth = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), 25, null);
				testSendEast = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), 25, null);
				testSendWest = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), 25, null);
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendDown, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendNorth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendSouth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendEast, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendWest, _tx);
							_tx.commit();
						}
					}
				}
			} else {
				testSendDown = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), 50, null);
				testSendNorth = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), 50, null);
				testSendSouth = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), 50, null);
				testSendEast = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), 50, null);
				testSendWest = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), 50, null);
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendDown, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendNorth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendSouth, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendEast, _tx);
							_tx.commit();
						}
					}
				}
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.insert((int) testSendWest, _tx);
							_tx.commit();
						}
					}
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