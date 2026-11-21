package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class RcpumptickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			if (((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.WATER || (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.WATER
					|| (world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.WATER || (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.WATER
					|| (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.WATER || (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.WATER)
					&& getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater") < 500) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcWater", 500);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			RcpipetickProcedure.execute(world, x, y, z);
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}