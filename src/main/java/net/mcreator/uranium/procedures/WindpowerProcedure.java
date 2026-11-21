package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class WindpowerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world instanceof Level _lvl0 && _lvl0.isRaining() || world instanceof Level _lvl1 && _lvl1.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert(50, _tx);
						_tx.commit();
					}
				}
			}
		} else if (!(world instanceof Level _lvl4 && _lvl4.isRaining() || world instanceof Level _lvl5 && _lvl5.isThundering()) && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))
				|| (world instanceof Level _lvl7 && _lvl7.isRaining() || world instanceof Level _lvl8 && _lvl8.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert(25, _tx);
						_tx.commit();
					}
				}
			}
		} else if (!(world instanceof Level _lvl11 && _lvl11.isRaining() || world instanceof Level _lvl12 && _lvl12.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			if (world instanceof ILevelExtension _ext) {
				if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
					try (var _tx = Transaction.openRoot()) {
						_energyHandler.insert(10, _tx);
						_tx.commit();
					}
				}
			}
		}
	}
}