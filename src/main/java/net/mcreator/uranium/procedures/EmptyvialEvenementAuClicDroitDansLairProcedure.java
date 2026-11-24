package net.mcreator.uranium.procedures;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModItems;

public class EmptyvialEvenementAuClicDroitDansLairProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (Blocks.WATER.defaultBlockState().getFluidState().isSource()) {
			if (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(4.5)), ClipContext.Block.VISUAL, ClipContext.Fluid.ANY, entity)).getType() == HitResult.Type.BLOCK
					&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == UraniumModItems.EMPTYVIAL.get()) {
				if (entity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("item.bucket.fill")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("item.bucket.fill")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				if (entity instanceof Player _player) {
					ItemStack _stktoremove = new ItemStack(UraniumModItems.EMPTYVIAL.get());
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
				}
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(UraniumModItems.WATERVIAL.get()).copy();
					_setstack.setCount(1);
					_player.getInventory().placeItemBackInInventory(_setstack);
				}
			}
		}
	}
}