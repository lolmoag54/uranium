package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;

public class WindturbineLorsDeLaMiseAJourDuTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		WindpowerProcedure.execute(world, x, y, z);
	}
}