package net.mcreator.uranium.procedures;

import net.minecraft.world.entity.Entity;

public class PoissonchattePlaybackCondition_swimProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.isSwimming()) {
			return true;
		}
		return false;
	}
}