package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;

public class CableOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			CableModelUpdaterMainProcedure.execute(world, x, y, z);
			CableForgeEnergyMainProcedure.execute(world, x, y, z);
		}
	}
}