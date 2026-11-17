package net.mcreator.uranium.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class FluidpushProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double f = 0;
		double c = 0;
		double nf = 0;
		double nc = 0;
		double m = 0;
		if (!world.isClientSide()) {
			f = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fluid");
			c = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "fluidCap");
			if (c > 0) {
				nc = getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "fluidCap");
				if (nc > 0) {
					nf = getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "fluid");
					m = ((f * nc - nf * c) / (c + nc)) / 4;
					if (m > 0.2 || m < -0.2) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x + 1, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", (nf + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						f = f - m;
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", f);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
				nc = getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "fluidCap");
				if (nc > 0) {
					nf = getBlockNBTNumber(world, BlockPos.containing(x, y + 1, z), "fluid");
					m = ((f * nc - nf * c) / (c + nc)) / 4;
					if (m > 0.2 || m < -0.2) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y + 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", (nf + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						f = f - m;
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", f);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
				nc = getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "fluidCap");
				if (nc > 0) {
					nf = getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "fluid");
					m = ((f * nc - nf * c) / (c + nc)) / 4;
					if (m > 0.2 || m < -0.2) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z + 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", (nf + m));
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						f = f - m;
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null) {
								_blockEntity.getPersistentData().putDouble("fluid", f);
							}
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
			}
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}