package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ProgressbarcentrifugeguiProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		BlockState etat_bloc = Blocks.AIR.defaultBlockState();
		etat_bloc = (world.getBlockState(BlockPos.containing(x, y, z)));
		if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip1 ? etat_bloc.getValue(_getip1) : -1) <= 0) {
			return 0;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip2 ? etat_bloc.getValue(_getip2) : -1) <= 50) {
			return 1;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip3 ? etat_bloc.getValue(_getip3) : -1) <= 100) {
			return 2;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip4 ? etat_bloc.getValue(_getip4) : -1) <= 150) {
			return 3;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip5 ? etat_bloc.getValue(_getip5) : -1) <= 200) {
			return 4;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip6 ? etat_bloc.getValue(_getip6) : -1) <= 250) {
			return 5;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip7 ? etat_bloc.getValue(_getip7) : -1) <= 300) {
			return 6;
		} else if ((getPropertyByName(etat_bloc, "centrifugation") instanceof IntegerProperty _getip8 ? etat_bloc.getValue(_getip8) : -1) <= 350) {
			return 7;
		}
		return 8;
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}