package net.mcreator.uranium.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.uranium.world.inventory.ReactorguiMenu;
import net.mcreator.uranium.procedures.ReactorblockaddedProcedure;
import net.mcreator.uranium.procedures.ReactorOnTickUpdateProcedure;
import net.mcreator.uranium.block.entity.ReactorBlockEntity;

import io.netty.buffer.Unpooled;

import com.mojang.serialization.MapCodec;

public class ReactorBlock extends FallingBlock implements EntityBlock {
	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final IntegerProperty BURN = IntegerProperty.create("burn", 0, 100);
	public static final MapCodec<ReactorBlock> CODEC = simpleCodec(ReactorBlock::new);

	@Override
	public MapCodec<ReactorBlock> codec() {
		return CODEC;
	}

	@Override
	public int getDustColor(BlockState blockstate, BlockGetter world, BlockPos pos) {
		return blockstate.getMapColor(world, pos).col;
	}

	public ReactorBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.break")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.step")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.place")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.hit")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.fall")))).strength(1f, 10f).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.IRON_XYLOPHONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(ON, false).setValue(BURN, 0));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	public int getLightDampening(BlockState state) {
		return 0;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ON, BURN);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(ON, false).setValue(BURN, 0);
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 1);
		ReactorblockaddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		ReactorOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		if (entity instanceof ServerPlayer player) {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("Fission reactor");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new ReactorguiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
				}
			}, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ReactorBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState blockstate, ServerLevel world, BlockPos blockpos, boolean flag) {
		Containers.updateNeighboursAfterDestroy(blockstate, world, blockpos);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos, Direction direction) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof ReactorBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}