package net.mcreator.uranium.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;

public class UraniumArmorEvenementDeTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, ItemStack itemstack) {
		if (itemstack.isDamaged()) {
			if (world instanceof ServerLevel _level) {
				itemstack.hurtAndBreak(1000000, _level, null, _stkprov -> {
				});
			}
			if (world instanceof Level _level && !_level.isClientSide())
				_level.explode(null, x, y, z, 100, Level.ExplosionInteraction.NONE);
		}
	}
}