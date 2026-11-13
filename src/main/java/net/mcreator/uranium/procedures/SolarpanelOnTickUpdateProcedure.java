package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class SolarpanelOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double testSendDown = 0;
		double testSendNorth = 0;
		double testSendSouth = 0;
		double testSendEast = 0;
		double testSendWest = 0;
		double maxSend = 0;
		double send = 0;
		double sendPosition = 0;
		BlockState mediumLevelBlock = Blocks.AIR.defaultBlockState();
		BlockState lowLevelBlock = Blocks.AIR.defaultBlockState();
		BlockState highLevelBlock = Blocks.AIR.defaultBlockState();
		if (!world.isClientSide()) {
			sendPosition = y - 1;
			maxSend = 64;
			if (canReceiveEnergy(world, BlockPos.containing(x, sendPosition, z), Direction.UP) && getBlockNBTLogic(world, BlockPos.containing(x, sendPosition, z), "cablesFCopperUp")) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, sendPosition, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("forgeFlow", 1024);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if (world instanceof Level _lvl4 && _lvl4.isRaining() && world instanceof Level _lvl5 && _lvl5.isThundering() && world instanceof Level _lvl6 && _lvl6.isBrightOutside()) {
					send = receiveEnergySimulate(world, BlockPos.containing(x, sendPosition, z), (int) (maxSend / 4), Direction.UP);
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, sendPosition, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) send, _tx);
								_tx.commit();
							}
						}
					}
				} else if ((!(world instanceof Level _lvl9 && _lvl9.isRaining()) && world instanceof Level _lvl10 && _lvl10.isThundering()
						|| world instanceof Level _lvl11 && _lvl11.isRaining() && !(world instanceof Level _lvl12 && _lvl12.isThundering())) && world instanceof Level _lvl13 && _lvl13.isBrightOutside()) {
					send = receiveEnergySimulate(world, BlockPos.containing(x, sendPosition, z), (int) (maxSend / 2), Direction.UP);
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, sendPosition, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) send, _tx);
								_tx.commit();
							}
						}
					}
				} else if (!(world instanceof Level _lvl16 && _lvl16.isRaining()) && !(world instanceof Level _lvl17 && _lvl17.isThundering()) && world instanceof Level _lvl18 && _lvl18.isBrightOutside()) {
					send = receiveEnergySimulate(world, BlockPos.containing(x, sendPosition, z), (int) maxSend, Direction.UP);
					if (world instanceof ILevelExtension _ext) {
						if (_ext.getCapability(Capabilities.Energy.BLOCK, BlockPos.containing(x, sendPosition, z), Direction.UP) instanceof EnergyHandler _energyHandler) {
							try (var _tx = Transaction.openRoot()) {
								_energyHandler.insert((int) send, _tx);
								_tx.commit();
							}
						}
					}
				}
			}
		}
	}

	private static boolean canReceiveEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler)
				return energyHandler.getCapacityAsLong() > 0;
		}
		return false;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			if (levelExtension.getCapability(Capabilities.Energy.BLOCK, pos, direction) instanceof EnergyHandler energyHandler) {
				try (var tx = Transaction.openRoot()) {
					return energyHandler.insert(amount, tx);
				}
			}
		}
		return 0;
	}
}