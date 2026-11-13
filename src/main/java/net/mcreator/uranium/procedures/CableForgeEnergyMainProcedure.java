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

import net.mcreator.uranium.network.UraniumModVariables;

public class CableForgeEnergyMainProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double extractEast = 0;
		double sendUp = 0;
		double energyLoss = 0;
		double sendSouth = 0;
		double sendWest = 0;
		double extractSouth = 0;
		double extractUp = 0;
		double extractNorth = 0;
		double sendDown = 0;
		double extractDown = 0;
		double sideAmount = 0;
		double sendEast = 0;
		double extractWest = 0;
		double sendNorth = 0;
		double sides = 0;
		boolean blockSouth = false;
		boolean blockNorth = false;
		boolean blockEast = false;
		boolean blockWest = false;
		boolean blockUp = false;
		boolean blockDown = false;
		String nbtNorthTagName = "";
		String nbtUpTagName = "";
		String nbtDownTagName = "";
		String nbtSouthTagName = "";
		String nbtWestTagName = "";
		String nbtEastTagName = "";
		if (!world.isClientSide()) {
			energyLoss = 0;
			nbtNorthTagName = "cablesFCopperNorth";
			nbtEastTagName = "cablesFCopperEast";
			nbtSouthTagName = "cablesFCopperSouth";
			nbtWestTagName = "cablesFCopperWest";
			nbtUpTagName = "cablesFCopperUp";
			nbtDownTagName = "cablesFCopperDown";
			if (UraniumModVariables.MapVariables.get(world).forgeFlow == 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("forgeFlow", 0);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (canExtractEnergy(world, BlockPos.containing(x, y, z), null) && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") > 0) {
				blockNorth = canReceiveEnergy(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH) && getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), nbtSouthTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				blockEast = canReceiveEnergy(world, BlockPos.containing(x + 1, y, z), Direction.WEST) && getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), nbtWestTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				blockSouth = canReceiveEnergy(world, BlockPos.containing(x, y, z + 1), Direction.NORTH) && getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), nbtNorthTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				blockWest = canReceiveEnergy(world, BlockPos.containing(x - 1, y, z), Direction.EAST) && getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), nbtEastTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				blockUp = canReceiveEnergy(world, BlockPos.containing(x, y + 1, z), Direction.DOWN) && getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), nbtDownTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				blockDown = canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.UP) && getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), nbtUpTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow");
				if (blockNorth) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z - 1);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (blockEast) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x + 1, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (blockSouth) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z + 1);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (blockWest) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x - 1, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (blockUp) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y + 1, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				if (blockDown) {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y - 1, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null) {
							_blockEntity.getPersistentData().putDouble("forgeFlow", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow") - 1));
						}
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
				sides = 0;
				if (blockNorth) {
					sides = sides + 1;
				}
				if (blockEast) {
					sides = sides + 1;
				}
				if (blockSouth) {
					sides = sides + 1;
				}
				if (blockWest) {
					sides = sides + 1;
				}
				if (blockUp) {
					sides = sides + 1;
				}
				if (blockDown) {
					sides = sides + 1;
				}
				sideAmount = getEnergyStored(world, BlockPos.containing(x, y, z), null) / sides;
				if (blockNorth) {
					extractNorth = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), (int) sideAmount, Direction.SOUTH);
					sendNorth = extractNorth - energyLoss;
					if (sendNorth >= 1 && getEnergyStored(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH) + sendNorth <= getMaxEnergyStored(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH) + sendNorth), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractNorth, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
				if (blockEast) {
					extractEast = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), (int) sideAmount, Direction.WEST);
					sendEast = extractEast - energyLoss;
					if (sendEast >= 1 && getEnergyStored(world, BlockPos.containing(x + 1, y, z), Direction.WEST) + sendEast <= getMaxEnergyStored(world, BlockPos.containing(x + 1, y, z), Direction.WEST)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), Direction.WEST) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x + 1, y, z), Direction.WEST) + sendEast), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractEast, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
				if (blockSouth) {
					extractSouth = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), (int) sideAmount, Direction.NORTH);
					sendSouth = extractSouth - energyLoss;
					if (sendSouth >= 1 && getEnergyStored(world, BlockPos.containing(x, y, z + 1), Direction.NORTH) + sendSouth <= getMaxEnergyStored(world, BlockPos.containing(x, y, z + 1), Direction.NORTH)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x, y, z + 1), Direction.NORTH) + sendSouth), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractSouth, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
				if (blockWest) {
					extractWest = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), (int) sideAmount, Direction.EAST);
					sendWest = extractWest - energyLoss;
					if (sendWest >= 1 && getEnergyStored(world, BlockPos.containing(x - 1, y, z), Direction.EAST) + sendWest <= getMaxEnergyStored(world, BlockPos.containing(x - 1, y, z), Direction.EAST)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), Direction.EAST) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x - 1, y, z), Direction.EAST) + sendWest), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractWest, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
				if (blockUp) {
					extractUp = receiveEnergySimulate(world, BlockPos.containing(x, y + 1, z), (int) sideAmount, Direction.DOWN);
					sendUp = extractUp - energyLoss;
					if (sendUp >= 1 && getEnergyStored(world, BlockPos.containing(x, y + 1, z), Direction.DOWN) + sendUp <= getMaxEnergyStored(world, BlockPos.containing(x, y + 1, z), Direction.DOWN)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y + 1, z), Direction.DOWN) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x, y + 1, z), Direction.DOWN) + sendUp), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractUp, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
				if (blockDown) {
					extractDown = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) sideAmount, Direction.UP);
					sendDown = extractDown - energyLoss;
					if (sendDown >= 1 && getEnergyStored(world, BlockPos.containing(x, y - 1, z), Direction.UP) + sendDown <= getMaxEnergyStored(world, BlockPos.containing(x, y - 1, z), Direction.UP)) {
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) (getEnergyStored(world, BlockPos.containing(x, y - 1, z), Direction.UP) + sendDown), _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) extractDown, _tx);
									_tx.commit();
								}
							}
						}
					}
				}
			}
		}
	}

	private static boolean canExtractEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getCapacityAsLong() > 0;
		}
		return false;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
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

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getAmountAsInt();
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

	public static int getMaxEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getCapacityAsInt();
		}
		return 0;
	}
}