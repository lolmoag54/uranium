package net.mcreator.uranium.procedures;

import net.minecraft.world.entity.Entity;

public class PoissonchatteOnEntityTickUpdateProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		PoissonchattePlaybackCondition_swimProcedure.execute(entity);
		PoissonchattePlaybackConditionProcedure.execute(entity);
	}
}