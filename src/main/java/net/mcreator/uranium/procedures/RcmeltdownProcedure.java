package net.mcreator.uranium.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

public class RcmeltdownProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double dx = 0;
		double dy = 0;
		double dz = 0;
		double rx = 0;
		double ry = 0;
		double rz = 0;
		if (!world.isClientSide()) {
			BlockPos _bp = BlockPos.containing(x, y, z);
			BlockEntity _blockEntity = world.getBlockEntity(_bp);
			BlockState _bs = world.getBlockState(_bp);
			if (_blockEntity != null) {
				_blockEntity.getPersistentData().putBoolean("rcScram", true);
				_blockEntity.getPersistentData().putDouble("rcDepth", 15);
				_blockEntity.getPersistentData().putDouble("rcHeat", 60);
			}
			if (world instanceof Level _level)
				_level.sendBlockUpdated(_bp, _bs, _bs, 3);
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.explode")), SoundSource.MASTER, 10, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.explode")), SoundSource.MASTER, 10, 1, false);
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.explode")), SoundSource.MASTER, 10, (float) 0.5);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.explode")), SoundSource.MASTER, 10, (float) 0.5, false);
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.lightning_bolt.thunder")), SoundSource.MASTER, 10, (float) 0.6);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.lightning_bolt.thunder")), SoundSource.MASTER, 10, (float) 0.6, false);
			}
		}
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 6, 6, 6, 6, 0);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 300, 8, 8, 8, 0.05);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.FLAME, x, y, z, 200, 8, 8, 8, 0.1);
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(null, x, y, z, 25, Level.ExplosionInteraction.TNT);
		dx = -12;
		for (int _i1 = 0; _i1 < 25; _i1++) {
			dy = -12;
			for (int _i2 = 0; _i2 < 25; _i2++) {
				dz = -12;
				for (int _i3 = 0; _i3 < 25; _i3++) {
					if (dx * dx + dy * dy + dz * dz <= 144) {
						world.setBlock(BlockPos.containing(x + dx, y + dy, z + dz), Blocks.AIR.defaultBlockState(), 3);
					}
					dz = dz + 1;
				}
				dy = dy + 1;
			}
			dx = dx + 1;
		}
		for (int _i1 = 0; _i1 < 500; _i1++) {
			rx = Mth.nextInt(RandomSource.create(), -14, 14);
			ry = Mth.nextInt(RandomSource.create(), -12, 2);
			rz = Mth.nextInt(RandomSource.create(), -14, 14);
			if (world.isEmptyBlock(BlockPos.containing(x + rx, y + ry, z + rz)) && !world.isEmptyBlock(BlockPos.containing(x + rx, (y + ry) - 1, z + rz))) {
				world.setBlock(BlockPos.containing(x + rx, y + ry, z + rz), Blocks.FIRE.defaultBlockState(), 3);
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(40 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof ItemEntity) {
					if (!entityiterator.level().isClientSide())
						entityiterator.discard();
				} else {
					{
						Entity _ent = entityiterator;
						if (_ent.level() instanceof ServerLevel _serverLevel) {
							_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.EXPLOSION)), 60);
						}
					}
				}
			}
		}
	}
}