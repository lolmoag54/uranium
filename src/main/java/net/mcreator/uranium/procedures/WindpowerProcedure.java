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
}