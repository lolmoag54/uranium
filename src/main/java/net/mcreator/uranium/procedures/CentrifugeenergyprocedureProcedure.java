package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class CentrifugeenergyprocedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double power = 0;
		double show_power = 0;
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z), null) && getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 50000) {
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.extract(300, _tx);
						_tx.commit();
					}
				}
			}
			power = getEnergyStored(world, BlockPos.containing(x, y, z), null);
			if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 0) {
				show_power = 0;
			} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 10000) {
				show_power = 1;
			} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 20000) {
				show_power = 2;
			} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 30000) {
				show_power = 3;
			} else if (getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 40000) {
				show_power = 4;
			}
			show_power = 5;
		}
	}

	private static boolean canReceiveEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getCapacityAsLong() > 0;
		}
		return false;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getAmountAsInt();
		}
		return 0;
	}
}