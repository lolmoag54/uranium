package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModItems;
import net.mcreator.uranium.init.UraniumModBlocks;

public class ReactorcraftingrecipeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 16).getCount() < 64 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 4).copy()).getItem() == Items.WATER_BUCKET && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 5).copy()).getItem() == UraniumModItems.ENRICHEDURANIUM.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 6).copy()).getItem() == UraniumModItems.LINGOTDEPLOMB.get() && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 7).copy()).getItem() == Items.WATER_BUCKET
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 8).copy()).getItem() == Items.WATER_BUCKET && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 9).copy()).getItem() == UraniumModItems.LINGOTDEPLOMB.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 10).copy()).getItem() == UraniumModItems.ENRICHEDURANIUM.get() && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 11).copy()).getItem() == Items.WATER_BUCKET
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 12).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 13).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 14).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 15).copy()).getItem() == UraniumModBlocks.REINFORCEDCONCRETE.get().asItem()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 16).getCount() <= 63 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 16).copy()).getItem() == UraniumModBlocks.REACTOR.get().asItem()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 16).copy()).getItem() == Blocks.AIR.asItem())) {
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(0).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(1).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(2).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(3).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(4).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(5).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(6).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(7).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(8).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(9).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(10).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(11).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(12).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(13).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(14).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					_container.getItem(15).shrink(1);
				}
			}
			if (world instanceof ServerLevel _serverLevel) {
				BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
				if (_be instanceof Container _container) {
					ItemStack _setstack = new ItemStack(UraniumModBlocks.REACTOR.get()).copy();
					_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 16).getCount() + 1);
					_container.setItem(16, _setstack);
				}
			}
		}
	}

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			ResourceHandler<ItemResource> itemHandler = ext.getCapability(Capabilities.Item.BLOCK, pos, null);
			if (itemHandler != null)
				return ItemUtil.getStack(itemHandler, slot);
		}
		return ItemStack.EMPTY;
	}
}