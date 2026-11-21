package net.mcreator.uranium.block.entity;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.init.UraniumModBlockEntities;
import net.mcreator.uranium.block.WindturbineBlock;

import javax.annotation.Nullable;

import java.util.stream.IntStream;

import com.geckolib.util.GeckoLibUtil;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.state.AnimationPoint;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.AnimationController;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.GeoBlockEntity;

public class WindturbineTileEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity, WorldlyContainer {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);

	public WindturbineTileEntity(BlockPos pos, BlockState state) {
		super(UraniumModBlockEntities.WINDTURBINE.get(), pos, state);
	}

	private PlayState predicate(AnimationTest<WindturbineTileEntity> state) {
		String animationprocedure = ("" + this.getBlockState().getValue(WindturbineBlock.ANIMATION));
		if (animationprocedure.equals("0")) {
			return state.setAndContinue(RawAnimation.begin().thenLoop(animationprocedure));
		}
		return PlayState.STOP;
	}

	String prevAnim = "0";
	boolean resumedAfterLoad = false;
	boolean needsInstantResume = false;

	private PlayState procedurePredicate(AnimationTest<WindturbineTileEntity> state) {
		String animationprocedure = ("" + this.getBlockState().getValue(WindturbineBlock.ANIMATION));
		if (!resumedAfterLoad) {
			resumedAfterLoad = true;
			if (!animationprocedure.equals("0"))
				needsInstantResume = true;
		} else if (needsInstantResume) {
			needsInstantResume = false;
			state.controller().setAnimationTime(1_000_000.0);
		}
		if ((!animationprocedure.equals("0") && hasPlayOnceAnimationFinished(state.controller())) || (!animationprocedure.equals(prevAnim) && !animationprocedure.equals("0"))) {
			if (!animationprocedure.equals(prevAnim))
				state.controller().reset();
			state.controller().setAnimation(RawAnimation.begin().thenPlay(animationprocedure));
			if (hasPlayOnceAnimationFinished(state.controller())) {
				if (this.getBlockState().getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp)
					level.setBlock(this.getBlockPos(), this.getBlockState().setValue(_integerProp, 0), 3);
				state.controller().reset();
			}
		} else if (animationprocedure.equals("0")) {
			prevAnim = "0";
			return PlayState.STOP;
		}
		prevAnim = animationprocedure;
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

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("controller", 0, this::predicate));
		data.add(new AnimationController<>("procedurecontroller", 0, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);
		if (!this.tryLoadLootTable(valueInput))
			this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(valueInput, this.stacks);
	}

	@Override
	public void saveAdditional(ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		if (!this.trySaveLootTable(valueOutput))
			ContainerHelper.saveAllItems(valueOutput, this.stacks);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return this.saveWithFullMetadata(lookupProvider);
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override
	public Component getDefaultName() {
		return Component.literal("windturbine");
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return ChestMenu.threeRows(id, inventory);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Wind turbine");
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.stacks;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return IntStream.range(0, this.getContainerSize()).toArray();
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return true;
	}
}