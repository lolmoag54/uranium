package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class CableOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("cableTimer", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cableTimer") + 1));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cableTimer") >= 5) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("cableTimer", 0);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				CableModelUpdaterMainProcedure.execute(world, x, y, z);
			}
			CableForgeEnergyMainProcedure.execute(world, x, y, z);
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}