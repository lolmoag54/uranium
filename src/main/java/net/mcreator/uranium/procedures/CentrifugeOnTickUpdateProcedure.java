package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModItems;

public class CentrifugeOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).getCount() <= 64 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == UraniumModItems.YELLOWCAKE.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() <= 63 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == Blocks.AIR.asItem()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == UraniumModItems.ENRICHEDURANIUM.get())
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() <= 63 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == Blocks.AIR.asItem()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == UraniumModItems.DEPLETEDURANIUM.get())) {
			{
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
					world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
			}
			if ((getPropertyByName(blockstate, "centrifugation") instanceof IntegerProperty _getip15 ? blockstate.getValue(_getip15) : -1) == 400) {
				if (world instanceof ServerLevel _serverLevel) {
					BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
					if (_be instanceof Container _container) {
						_container.getItem(0).shrink(1);
					}
				}
				if (Mth.nextDouble(RandomSource.create(), 1, 10) <= 2) {
					if (world instanceof ServerLevel _serverLevel) {
						BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
						if (_be instanceof Container _container) {
							ItemStack _setstack = new ItemStack(UraniumModItems.ENRICHEDURANIUM.get()).copy();
							_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).getCount() + 1);
							_container.setItem(1, _setstack);
						}
					}
					if (world instanceof ServerLevel _serverLevel) {
						BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
						if (_be instanceof Container _container) {
							ItemStack _setstack = new ItemStack(UraniumModItems.DEPLETEDURANIUM.get()).copy();
							_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() + 1);
							_container.setItem(2, _setstack);
						}
					}
				} else {
					if (world instanceof ServerLevel _serverLevel) {
						BlockEntity _be = _serverLevel.getBlockEntity(BlockPos.containing(x, y, z));
						if (_be instanceof Container _container) {
							ItemStack _setstack = new ItemStack(UraniumModItems.DEPLETEDURANIUM.get()).copy();
							_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() + 2);
							_container.setItem(2, _setstack);
						}
					}
				}
				{
					int _value = 0;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("centrifugation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			} else {
				{
					int _value = (getPropertyByName(blockstate, "centrifugation") instanceof IntegerProperty _getip26 ? blockstate.getValue(_getip26) : -1) + 10;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("centrifugation") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			}
		}
		if ((itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == Blocks.AIR.asItem()) {
			{
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("on") instanceof BooleanProperty _booleanProp)
					world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
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

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}