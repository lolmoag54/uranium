package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.network.UraniumModVariables;

public class GrossebatterieOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double north = 0;
		double south = 0;
		double east = 0;
		double west = 0;
		double down = 0;
		double send = 0;
		double capacity = 0;
		String nbtSouthTagName = "";
		String nbtWestTagName = "";
		String nbtNorthTagName = "";
		String nbtEastTagName = "";
		String nbtUpTagName = "";
		if (!world.isClientSide()) {
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
				nbtNorthTagName = "cablesFCopperNorth";
				nbtEastTagName = "cablesFCopperEast";
				nbtSouthTagName = "cablesFCopperSouth";
				nbtWestTagName = "cablesFCopperWest";
				nbtUpTagName = "cablesFCopperUp";
				if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.NORTH && canReceiveEnergy(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH)
						&& getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), nbtSouthTagName) && getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
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
				} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.EAST && canReceiveEnergy(world, BlockPos.containing(x + 1, y, z), Direction.WEST)
						&& getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), nbtWestTagName) && getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
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
				} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.SOUTH && canReceiveEnergy(world, BlockPos.containing(x, y, z + 1), Direction.NORTH)
						&& getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), nbtNorthTagName) && getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
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
				} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.WEST && canReceiveEnergy(world, BlockPos.containing(x - 1, y, z), Direction.EAST)
						&& getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), nbtEastTagName) && getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
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
				if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.UP) && getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), nbtUpTagName)
						&& getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
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
				if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= 128) {
					if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.NORTH && canReceiveEnergy(world, BlockPos.containing(x, y, z - 1), Direction.SOUTH)
							&& getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), nbtSouthTagName)
							&& getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
						send = receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), 64, Direction.SOUTH);
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) send, _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) send, _tx);
									_tx.commit();
								}
							}
						}
					} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.EAST && canReceiveEnergy(world, BlockPos.containing(x + 1, y, z), Direction.WEST)
							&& getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), nbtWestTagName)
							&& getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
						send = receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), 64, Direction.WEST);
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) send, _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x + 1, y, z), Direction.WEST) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) send, _tx);
									_tx.commit();
								}
							}
						}
					} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.SOUTH && canReceiveEnergy(world, BlockPos.containing(x, y, z + 1), Direction.NORTH)
							&& getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), nbtNorthTagName)
							&& getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
						send = receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), 64, Direction.NORTH);
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) send, _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) send, _tx);
									_tx.commit();
								}
							}
						}
					} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.WEST && canReceiveEnergy(world, BlockPos.containing(x - 1, y, z), Direction.EAST)
							&& getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), nbtEastTagName)
							&& getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
						send = receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), 64, Direction.EAST);
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) send, _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x - 1, y, z), Direction.EAST) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) send, _tx);
									_tx.commit();
								}
							}
						}
					}
					if (canReceiveEnergy(world, BlockPos.containing(x, y - 1, z), Direction.UP) && getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), nbtUpTagName)
							&& getBlockNBTNumber(world, BlockPos.containing(x, y - 1, z), "forgeFlow") < getBlockNBTNumber(world, BlockPos.containing(x, y, z), "forgeFlow")) {
						send = receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), 64, Direction.UP);
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.extract((int) send, _tx);
									_tx.commit();
								}
							}
						}
						if (world instanceof ILevelExtension _ext) {
							if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
								try (var _tx = Transaction.openRoot()) {
									_energyHandler.insert((int) send, _tx);
									_tx.commit();
								}
							}
						}
					}
					capacity = getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 5;
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

	private static Direction getDirectionFromBlockState(BlockState blockState) {
		if (getPropertyByName(blockState, "facing") instanceof EnumProperty ep && ep.getValueClass() == Direction.class)
			return (Direction) blockState.getValue(ep);
		if (getPropertyByName(blockState, "axis") instanceof EnumProperty ep && ep.getValueClass() == Direction.Axis.class)
			return Direction.fromAxisAndDirection((Direction.Axis) blockState.getValue(ep), Direction.AxisDirection.POSITIVE);
		return Direction.NORTH;
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

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}