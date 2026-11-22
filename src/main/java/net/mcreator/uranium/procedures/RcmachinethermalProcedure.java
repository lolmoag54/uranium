package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class RcmachinethermalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double h = 0;
		double h0 = 0;
		double w = 0;
		double w0 = 0;
		double g = 0;
		double c = 0;
		if (!world.isClientSide()) {
			if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcMInit")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("rcMInit", true);
						_blockEntity.getPersistentData().putBoolean("rcFluid", true);
						_blockEntity.getPersistentData().putBoolean("rcSink", true);
						_blockEntity.getPersistentData().putDouble("rcCap", 4000);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			h = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcHeat");
			h0 = h;
			w = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "rcWater");
			w0 = w;
			g = 0;
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcWorking") && !getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcHot")) {
				g = 0.5;
			}
			c = 0.04 + h * 0.003;
			if (w > 0 && h > 40) {
				c = c + 0.6;
				w = w - 1;
				if (w < 0) {
					w = 0;
				}
			}
			h = (h + g) - c;
			if (h < 0) {
				h = 0;
			}
			if (h > 100) {
				h = 100;
			}
			if (h >= 95 && !getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcHot")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("rcHot", true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (h <= 60 && getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcHot")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("rcHot", false);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (!(h == h0)) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcHeat", h);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (!(w == w0)) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("rcWater", w);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "rcHot")) {
				if (world instanceof ILevelExtension _ext) {
					if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, y, z), null) instanceof EnergyHandler _energyHandler) {
						try (var _tx = Transaction.openRoot()) {
							_energyHandler.extract(1000000, _tx);
							_tx.commit();
						}
					}
				}
			}
		}
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}