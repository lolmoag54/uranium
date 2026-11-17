package net.mcreator.uranium.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class PoissonchattePlaybackConditionProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.isInWater() && entity.isUnderWater() && entity instanceof Mob _mobEnt2 && _mobEnt2.isAggressive() && entity instanceof LivingEntity _livEnt3 && _livEnt3.swinging) {
			return true;
		}
		return false;
	}
}