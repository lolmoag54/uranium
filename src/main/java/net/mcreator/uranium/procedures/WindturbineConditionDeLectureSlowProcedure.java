package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class WindturbineConditionDeLectureSlowProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (!(world instanceof Level _lvl0 && _lvl0.isRaining() || world instanceof Level _lvl1 && _lvl1.isThundering()) && !world.canSeeSkyFromBelowWater(BlockPos.containing(x, y - 1, z))) {
			return true;
		}
		return false;
	}
}