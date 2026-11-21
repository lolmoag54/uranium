package net.mcreator.uranium.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.uranium.init.UraniumModEntities;
import net.mcreator.uranium.UraniumMod;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import com.geckolib.util.GeckoLibUtil;
import com.geckolib.cache.animation.keyframeevent.ParticleKeyframeData;
import com.geckolib.animation.state.KeyFrameEvent;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.state.AnimationPoint;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.AnimationController;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.GeoEntity;

public class CrystalstagEntity extends PathfinderMob implements GeoEntity {
	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(CrystalstagEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(CrystalstagEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(CrystalstagEntity.class, EntityDataSerializers.STRING);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
	public String animationprocedure = "empty";
	private String procedureControllerRequester = "";

	public CrystalstagEntity(EntityType<CrystalstagEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHOOT, false);
		builder.define(ANIMATION, "undefined");
		builder.define(TEXTURE, "cerf_cristallin");
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.5) {
			@Override
			protected boolean findRandomPosition() {
				Vec3 avoid = null;
				LivingEntity attacker = this.mob.getLastHurtByMob();
				if (attacker != null) {
					avoid = attacker.position();
				} else if (this.mob.getLastDamageSource() != null) {
					Entity src = this.mob.getLastDamageSource().getEntity();
					if (src != null) {
						avoid = src.position();
					} else if (this.mob.getLastDamageSource().getSourcePosition() != null) {
						avoid = this.mob.getLastDamageSource().getSourcePosition();
					}
				}
				Vec3 pos = null;
				if (avoid != null) {
					pos = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPosAway(this.mob, 10, 4, avoid);
				}
				if (pos == null) {
					pos = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPos(this.mob, 10, 4);
				}
				if (pos == null) {
					return false;
				}
				this.posX = pos.x;
				this.posY = pos.y;
				this.posZ = pos.z;
				return true;
			}
		});
		this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(2, new PanicGoal(this, 2));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));
		this.goalSelector.addGoal(6, new LeapAtTargetGoal(this, (float) 0.5));
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.death"));
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.IN_FIRE))
			return false;
		if (damagesource.getDirectEntity() instanceof AbstractThrownPotion || damagesource.getDirectEntity() instanceof AreaEffectCloud || damagesource.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
			return false;
		if (damagesource.is(DamageTypes.FALLING_ANVIL))
			return false;
		if (damagesource.is(DamageTypes.DRAGON_BREATH))
			return false;
		return super.hurtServer(level, damagesource, amount);
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putString("AnimationProcedure", this.entityData.get(ANIMATION));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "cerf_cristallin"));
		this.entityData.set(ANIMATION, valueInput.getStringOr("AnimationProcedure", "undefined"));
	}

	@Override
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.SUCCESS;
		super.mobInteract(sourceentity, hand);
		sourceentity.startRiding(this);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.refreshDimensions();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose).scale(1f);
	}

	@Override
	public void travel(Vec3 travelVector) {
		Entity rider = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
		if (this.isVehicle() && rider != null) {
			this.setYRot(rider.getYRot());
			this.yRotO = this.getYRot();
			this.setXRot(rider.getXRot() * 0.5F);
			this.setRot(this.getYRot(), this.getXRot());
			this.yBodyRot = rider.getYRot();
			this.yHeadRot = rider.getYRot();
			if (rider instanceof LivingEntity passenger) {
				this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
				float forward = passenger.zza;
				float strafe = passenger.xxa;
				super.travel(new Vec3(strafe, 0, forward));
			}
			double d1 = this.getX() - this.xo;
			double d0 = this.getZ() - this.zo;
			float f1 = (float) Math.sqrt(d1 * d1 + d0 * d0) * 4;
			if (f1 > 1.0F)
				f1 = 1.0F;
			this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4F);
			this.walkAnimation.position(this.walkAnimation.position() + this.walkAnimation.speed());
			this.calculateEntityAnimation(true);
			return;
		}
		super.travel(travelVector);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.updateSwingTime();
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
		event.register(UraniumModEntities.CRYSTALSTAG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		return builder;
	}

	private PlayState movementPredicate(AnimationTest<CrystalstagEntity> event) {
		if (this.animationprocedure.equals("empty")) {
			if (event.isMoving()

			) {
				return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
			}
			return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
		}
		return PlayState.STOP;
	}

	String prevAnim = "empty";

	private PlayState procedurePredicate(AnimationTest<CrystalstagEntity> event) {
		if (!animationprocedure.equals("empty") && hasPlayOnceAnimationFinished(event.controller()) || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
			if (!this.animationprocedure.equals(prevAnim))
				event.controller().reset();
			event.controller().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (hasPlayOnceAnimationFinished(event.controller())) {
				this.animationprocedure = "empty";
				event.controller().reset();
			}
		} else if (animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	private static boolean hasPlayOnceAnimationFinished(AnimationController<?> controller) {
		if (!controller.hasAnimationFinished())
			return false;
		AnimationPoint point = controller.getCurrentAnimationPoint();
		if (point == null)
			return true;
		LoopType loopType = point.loopType() == LoopType.DEFAULT ? point.animation().loopType() : point.loopType();
		return loopType != LoopType.HOLD_ON_LAST_FRAME && loopType != LoopType.LOOP;
	}

	/**
	 * Custom death animation length. XP/loot are already dropped by LivingEntity#die
	 * via dropAllDeathLoot -> dropExperience(ServerLevel, Entity); do not re-drop here.
	 */
	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 20) {
			this.remove(CrystalstagEntity.RemovalReason.KILLED);
		}
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
		this.animationprocedure = animation;
		this.procedureControllerRequester = "";
	}

	/**
	 * Moves whatever procedures wrote into the synched controller slots over to the
	 * fields the AnimationControllers read, the same way EntityAnimationFactory
	 * handles the built-in "procedure" controller.
	 */
	public void applySyncedControllerAnimations() {
	}

	/**
	 * Plays an animation on a named controller. A blank or unknown controller name
	 * falls back to the built-in "procedure" controller, so procedures written
	 * before custom controllers existed keep behaving exactly as before.
	 *
	 * <p>Stopping ("empty") an unknown controller name only stops the "procedure"
	 * controller if that same name started what it is playing. Otherwise a tick procedure
	 * that keeps stopping a name nothing else uses would also cancel every other
	 * procedure animation, such as spawn or right-click ones.
	 */
	public void setControllerAnimation(String controller, String animation) {
		if (controller == null || controller.isBlank()) {
			this.setAnimation(animation);
			return;
		}
		switch (controller) {
			default -> {
				if ("empty".equals(animation) && !controller.equals(this.procedureControllerRequester))
					return;
				this.setAnimation(animation);
				this.procedureControllerRequester = controller;
			}
		}
	}

	/** Currently playing animation on a named controller, or "empty" if idle. */
	public String getControllerAnimation(String controller) {
		if (controller == null || controller.isBlank())
			return this.animationprocedure;
		return switch (controller) {
			default -> this.animationprocedure;
		};
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("movement", 4, this::movementPredicate).setParticleKeyframeHandler(this::spawnKeyframeParticle));
		data.add(new AnimationController<>("procedure", 4, this::procedurePredicate).setParticleKeyframeHandler(this::spawnKeyframeParticle));
	}

	private static final Set<String> UNKNOWN_PARTICLE_EFFECTS = ConcurrentHashMap.newKeySet();

	private void spawnKeyframeParticle(KeyFrameEvent<CrystalstagEntity, ParticleKeyframeData> event) {
		String effect = event.keyframeData().getEffect();
		Identifier id = Identifier.tryParse(effect);
		if (id != null && BuiltInRegistries.PARTICLE_TYPE.getValue(id) instanceof SimpleParticleType particle)
			this.level().addParticle(particle, this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), 0, 0, 0);
		else if (UNKNOWN_PARTICLE_EFFECTS.add(effect))
			UraniumMod.LOGGER.warn("Particle keyframe effect \"{}\" in Crystalstag animations is not a registered particle id, so nothing is spawned", effect);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}