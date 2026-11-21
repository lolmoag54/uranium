package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;

public class RcpipeupdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			RcpipetickProcedure.execute(world, x, y, z);
			RcpipeshapeProcedure.execute(world, x, y, z);
		}
	}
}