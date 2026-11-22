<<<<<<< HEAD
package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.network.UraniumModVariables;

public class WindpowerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			UraniumModVariables.MapVariables.get(world).forgeFlow = y - 1;
			UraniumModVariables.MapVariables.get(world).forgeFlow = 64;
			UraniumModVariables.MapVariables.get(world).markSyncDirty();
			if (canReceiveEnergy(world, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), Direction.UP)
					&& getBlockNBTLogic(world, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), "cablesFCopperUp")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if ((world instanceof Level _lvl4 && _lvl4.isRaining() || world instanceof Level _lvl5 && _lvl5.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
					{
						int _value = 2;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					UraniumModVariables.MapVariables.get(world).forgeFlow = receiveEnergySimulate(world, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z),
							(int) (UraniumModVariables.MapVariables.get(world).forgeFlow / 4), Direction.UP);
					UraniumModVariables.MapVariables.get(world).markSyncDirty();
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) UraniumModVariables.MapVariables.get(world).forgeFlow, _tx);
								_tx.commit();
							}
						}
					}
				} else if (!(world instanceof Level _lvl10 && _lvl10.isRaining() || world instanceof Level _lvl11 && _lvl11.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					UraniumModVariables.MapVariables.get(world).forgeFlow = receiveEnergySimulate(world, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z),
							(int) (UraniumModVariables.MapVariables.get(world).forgeFlow / 2), Direction.UP);
					UraniumModVariables.MapVariables.get(world).markSyncDirty();
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) UraniumModVariables.MapVariables.get(world).forgeFlow, _tx);
								_tx.commit();
							}
						}
					}
				} else if (!(world instanceof Level _lvl16 && _lvl16.isRaining() || world instanceof Level _lvl17 && _lvl17.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))
						|| (world instanceof Level _lvl19 && _lvl19.isRaining() || world instanceof Level _lvl20 && _lvl20.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					UraniumModVariables.MapVariables.get(world).forgeFlow = receiveEnergySimulate(world, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), (int) UraniumModVariables.MapVariables.get(world).forgeFlow,
							Direction.UP);
					UraniumModVariables.MapVariables.get(world).markSyncDirty();
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, UraniumModVariables.MapVariables.get(world).forgeFlow, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) UraniumModVariables.MapVariables.get(world).forgeFlow, _tx);
								_tx.commit();
							}
						}
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
=======
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
>>>>>>> branch 'master' of https://github.com/lolmoag54/uranium
}