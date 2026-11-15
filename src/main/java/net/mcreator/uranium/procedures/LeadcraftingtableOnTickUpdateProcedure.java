package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;

public class LeadcraftingtableOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ReactorcraftingrecipeProcedure.execute(world, x, y, z);
	}
}