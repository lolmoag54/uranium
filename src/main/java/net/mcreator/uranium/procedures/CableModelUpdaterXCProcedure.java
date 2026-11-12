package net.mcreator.uranium.procedures;

import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.ProblemReporter;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModBlocks;

public class CableModelUpdaterXCProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String tagPath = "";
		String nbtSouthTagName = "";
		String nbtWestTagName = "";
		String nbtNorthTagName = "";
		String nbtEastTagName = "";
		String nbtUpTagName = "";
		String nbtDownTagName = "";
		BlockState blockSelector = Blocks.AIR.defaultBlockState();
		if (!world.isClientSide()) {
			blockSelector = UraniumModBlocks.CABLE_XC.get().defaultBlockState();
			tagPath = "fe_system:cables/f/copper";
			nbtNorthTagName = "cablesFCopperNorth";
			nbtEastTagName = "cablesFCopperEast";
			nbtSouthTagName = "cablesFCopperSouth";
			nbtWestTagName = "cablesFCopperWest";
			nbtUpTagName = "cablesFCopperUp";
			nbtDownTagName = "cablesFCopperDown";
			if (((world.getBlockState(BlockPos.containing(x, y + 1, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), nbtDownTagName))
					&& ((world.getBlockState(BlockPos.containing(x, y - 1, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), nbtUpTagName))
					&& ((world.getBlockState(BlockPos.containing(x, y, z + 1))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), nbtNorthTagName))
					&& ((world.getBlockState(BlockPos.containing(x, y, z - 1))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), nbtSouthTagName))
					&& !(world.getBlockState(BlockPos.containing(x + 1, y, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) && !getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), nbtWestTagName)
					&& !(world.getBlockState(BlockPos.containing(x - 1, y, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) && !getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), nbtEastTagName)) {
				{
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockState _bs = blockSelector;
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					BlockEntity _be = world.getBlockEntity(_bp);
					CompoundTag _bnbt = null;
					if (_be != null) {
						_bnbt = _be.saveWithFullMetadata(world.registryAccess());
						_be.setRemoved();
					}
					world.setBlock(_bp, _bs, 3);
					if (_bnbt != null) {
						_be = world.getBlockEntity(_bp);
						if (_be != null) {
							try {
								_be.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), _bnbt));
							} catch (Exception ignored) {
							}
						}
					}
				}
				{
					Direction _dir = Direction.SOUTH;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("facing") instanceof EnumProperty _dp && _dp.getPossibleValues().contains(_dir)) {
						world.setBlock(_pos, _bs.setValue(_dp, _dir), 3);
					} else if (_bs.getBlock().getStateDefinition().getProperty("axis") instanceof EnumProperty _ap && _ap.getPossibleValues().contains(_dir.getAxis())) {
						world.setBlock(_pos, _bs.setValue(_ap, _dir.getAxis()), 3);
					}
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean(nbtUpTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtDownTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtNorthTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtEastTagName, false);
						_blockEntity.getPersistentData().putBoolean(nbtSouthTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtWestTagName, false);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if (((world.getBlockState(BlockPos.containing(x, y + 1, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y + 1, z), nbtDownTagName))
					&& ((world.getBlockState(BlockPos.containing(x, y - 1, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x, y - 1, z), nbtUpTagName))
					&& !(world.getBlockState(BlockPos.containing(x, y, z + 1))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) && !getBlockNBTLogic(world, BlockPos.containing(x, y, z + 1), nbtNorthTagName)
					&& !(world.getBlockState(BlockPos.containing(x, y, z - 1))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) && !getBlockNBTLogic(world, BlockPos.containing(x, y, z - 1), nbtSouthTagName)
					&& ((world.getBlockState(BlockPos.containing(x + 1, y, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x + 1, y, z), nbtWestTagName))
					&& ((world.getBlockState(BlockPos.containing(x - 1, y, z))).is(BlockTags.create(Identifier.parse((tagPath).toLowerCase(java.util.Locale.ENGLISH)))) || getBlockNBTLogic(world, BlockPos.containing(x - 1, y, z), nbtEastTagName))) {
				{
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockState _bs = blockSelector;
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					BlockEntity _be = world.getBlockEntity(_bp);
					CompoundTag _bnbt = null;
					if (_be != null) {
						_bnbt = _be.saveWithFullMetadata(world.registryAccess());
						_be.setRemoved();
					}
					world.setBlock(_bp, _bs, 3);
					if (_bnbt != null) {
						_be = world.getBlockEntity(_bp);
						if (_be != null) {
							try {
								_be.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), _bnbt));
							} catch (Exception ignored) {
							}
						}
					}
				}
				{
					Direction _dir = Direction.EAST;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("facing") instanceof EnumProperty _dp && _dp.getPossibleValues().contains(_dir)) {
						world.setBlock(_pos, _bs.setValue(_dp, _dir), 3);
					} else if (_bs.getBlock().getStateDefinition().getProperty("axis") instanceof EnumProperty _ap && _ap.getPossibleValues().contains(_dir.getAxis())) {
						world.setBlock(_pos, _bs.setValue(_ap, _dir.getAxis()), 3);
					}
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean(nbtUpTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtDownTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtNorthTagName, false);
						_blockEntity.getPersistentData().putBoolean(nbtEastTagName, true);
						_blockEntity.getPersistentData().putBoolean(nbtSouthTagName, false);
						_blockEntity.getPersistentData().putBoolean(nbtWestTagName, true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
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
}