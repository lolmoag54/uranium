package net.mcreator.uranium.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.*;
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

import java.util.function.Function;

import io.netty.buffer.Unpooled;

import com.mojang.serialization.MapCodec;

public class ReactorBlock extends FallingBlock implements EntityBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final IntegerProperty BURN = IntegerProperty.create("burn", 0, 100);
	private final Function<BlockState, VoxelShape> shapes = this.makeShapes();
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
		super(properties
				.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.break")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.step")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.place")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.hit")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.fall"))))
				.strength(1f, 10f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false).instrument(NoteBlockInstrument.IRON_XYLOPHONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ON, false).setValue(BURN, 0));
	}

	private Function<BlockState, VoxelShape> makeShapes() {
		return this.getShapeForEachState(state -> {
			return switch (state.getValue(FACING)) {
				case NORTH -> Shapes.or(box(0, 0, 0, 16, 10, 16), box(9.2, 13, 9, 14.75, 13.5, 14.25), box(8.25, 9, 8, 15.75, 10, 15.25), box(8.3, 10, 8.05, 15.7, 10.25, 15.2), box(8.6, 14.9, 8.4, 15.35, 15.8, 14.95),
						box(9.05, 13.5, 8.8, 14.9, 14, 14.45), box(9.2, 12.5, 9, 14.75, 13, 14.25), box(8.7, 14.7, 8.45, 15.3, 14.95, 14.8), box(8.4, 10.25, 8.15, 15.6, 10.5, 15.1), box(8.5, 10.5, 8.25, 15.5, 10.75, 15),
						box(8.6, 10.75, 8.35, 15.4, 11, 14.9), box(8.75, 14.3, 8.5, 15.2, 14.8, 14.75), box(8.85, 14, 8.6, 15.1, 14.5, 14.65), box(9.05, 12, 8.8, 14.9, 12.5, 14.45), box(8.85, 11.5, 8.6, 15.1, 12, 14.65),
						box(8.75, 11, 8.5, 15.2, 11.5, 14.75), box(8.7, 11, 8.45, 15.3, 11.25, 14.8), box(1.2, 13, 9, 6.75, 13.5, 14.25), box(0.25, 9, 8, 7.75, 10, 15.25), box(0.3, 10, 8.05, 7.7, 10.25, 15.2), box(0.6, 14.9, 8.4, 7.35, 15.8, 14.95),
						box(1.05, 13.5, 8.8, 6.9, 14, 14.45), box(1.2, 12.5, 9, 6.75, 13, 14.25), box(0.7, 14.7, 8.45, 7.3, 14.95, 14.8), box(0.4, 10.25, 8.15, 7.6, 10.5, 15.1), box(0.5, 10.5, 8.25, 7.5, 10.75, 15),
						box(0.6, 10.75, 8.35, 7.4, 11, 14.9), box(0.75, 14.3, 8.5, 7.2, 14.8, 14.75), box(0.85, 14, 8.6, 7.1, 14.5, 14.65), box(1.05, 12, 8.8, 6.9, 12.5, 14.45), box(0.85, 11.5, 8.6, 7.1, 12, 14.65),
						box(0.75, 11, 8.5, 7.2, 11.5, 14.75), box(0.7, 11, 8.45, 7.3, 11.25, 14.8), box(0, 0, 0, 16, 16, 16));
				case EAST -> Shapes.or(box(0, 0, 0, 16, 10, 16), box(1.75, 13, 9.2, 7, 13.5, 14.75), box(0.75, 9, 8.25, 8, 10, 15.75), box(0.8, 10, 8.3, 7.95, 10.25, 15.7), box(1.05, 14.9, 8.6, 7.6, 15.8, 15.35), box(1.55, 13.5, 9.05, 7.2, 14, 14.9),
						box(1.75, 12.5, 9.2, 7, 13, 14.75), box(1.2, 14.7, 8.7, 7.55, 14.95, 15.3), box(0.9, 10.25, 8.4, 7.85, 10.5, 15.6), box(1, 10.5, 8.5, 7.75, 10.75, 15.5), box(1.1, 10.75, 8.6, 7.65, 11, 15.4),
						box(1.25, 14.3, 8.75, 7.5, 14.8, 15.2), box(1.35, 14, 8.85, 7.4, 14.5, 15.1), box(1.55, 12, 9.05, 7.2, 12.5, 14.9), box(1.35, 11.5, 8.85, 7.4, 12, 15.1), box(1.25, 11, 8.75, 7.5, 11.5, 15.2),
						box(1.2, 11, 8.7, 7.55, 11.25, 15.3), box(1.75, 13, 1.2, 7, 13.5, 6.75), box(0.75, 9, 0.25, 8, 10, 7.75), box(0.8, 10, 0.3, 7.95, 10.25, 7.7), box(1.05, 14.9, 0.6, 7.6, 15.8, 7.35), box(1.55, 13.5, 1.05, 7.2, 14, 6.9),
						box(1.75, 12.5, 1.2, 7, 13, 6.75), box(1.2, 14.7, 0.7, 7.55, 14.95, 7.3), box(0.9, 10.25, 0.4, 7.85, 10.5, 7.6), box(1, 10.5, 0.5, 7.75, 10.75, 7.5), box(1.1, 10.75, 0.6, 7.65, 11, 7.4), box(1.25, 14.3, 0.75, 7.5, 14.8, 7.2),
						box(1.35, 14, 0.85, 7.4, 14.5, 7.1), box(1.55, 12, 1.05, 7.2, 12.5, 6.9), box(1.35, 11.5, 0.85, 7.4, 12, 7.1), box(1.25, 11, 0.75, 7.5, 11.5, 7.2), box(1.2, 11, 0.7, 7.55, 11.25, 7.3), box(0, 0, 0, 16, 16, 16));
				case WEST -> Shapes.or(box(0, 0, 0, 16, 10, 16), box(9, 13, 1.25, 14.25, 13.5, 6.8), box(8, 9, 0.25, 15.25, 10, 7.75), box(8.05, 10, 0.3, 15.2, 10.25, 7.7), box(8.4, 14.9, 0.65, 14.95, 15.8, 7.4), box(8.8, 13.5, 1.1, 14.45, 14, 6.95),
						box(9, 12.5, 1.25, 14.25, 13, 6.8), box(8.45, 14.7, 0.7, 14.8, 14.95, 7.3), box(8.15, 10.25, 0.4, 15.1, 10.5, 7.6), box(8.25, 10.5, 0.5, 15, 10.75, 7.5), box(8.35, 10.75, 0.6, 14.9, 11, 7.4),
						box(8.5, 14.3, 0.8, 14.75, 14.8, 7.25), box(8.6, 14, 0.9, 14.65, 14.5, 7.15), box(8.8, 12, 1.1, 14.45, 12.5, 6.95), box(8.6, 11.5, 0.9, 14.65, 12, 7.15), box(8.5, 11, 0.8, 14.75, 11.5, 7.25),
						box(8.45, 11, 0.7, 14.8, 11.25, 7.3), box(9, 13, 9.25, 14.25, 13.5, 14.8), box(8, 9, 8.25, 15.25, 10, 15.75), box(8.05, 10, 8.3, 15.2, 10.25, 15.7), box(8.4, 14.9, 8.65, 14.95, 15.8, 15.4),
						box(8.8, 13.5, 9.1, 14.45, 14, 14.95), box(9, 12.5, 9.25, 14.25, 13, 14.8), box(8.45, 14.7, 8.7, 14.8, 14.95, 15.3), box(8.15, 10.25, 8.4, 15.1, 10.5, 15.6), box(8.25, 10.5, 8.5, 15, 10.75, 15.5),
						box(8.35, 10.75, 8.6, 14.9, 11, 15.4), box(8.5, 14.3, 8.8, 14.75, 14.8, 15.25), box(8.6, 14, 8.9, 14.65, 14.5, 15.15), box(8.8, 12, 9.1, 14.45, 12.5, 14.95), box(8.6, 11.5, 8.9, 14.65, 12, 15.15),
						box(8.5, 11, 8.8, 14.75, 11.5, 15.25), box(8.45, 11, 8.7, 14.8, 11.25, 15.3), box(0, 0, 0, 16, 16, 16));
				default -> Shapes.or(box(0, 0, 0, 16, 10, 16), box(1.25, 13, 1.75, 6.8, 13.5, 7), box(0.25, 9, 0.75, 7.75, 10, 8), box(0.3, 10, 0.8, 7.7, 10.25, 7.95), box(0.65, 14.9, 1.05, 7.4, 15.8, 7.6), box(1.1, 13.5, 1.55, 6.95, 14, 7.2),
						box(1.25, 12.5, 1.75, 6.8, 13, 7), box(0.7, 14.7, 1.2, 7.3, 14.95, 7.55), box(0.4, 10.25, 0.9, 7.6, 10.5, 7.85), box(0.5, 10.5, 1, 7.5, 10.75, 7.75), box(0.6, 10.75, 1.1, 7.4, 11, 7.65), box(0.8, 14.3, 1.25, 7.25, 14.8, 7.5),
						box(0.9, 14, 1.35, 7.15, 14.5, 7.4), box(1.1, 12, 1.55, 6.95, 12.5, 7.2), box(0.9, 11.5, 1.35, 7.15, 12, 7.4), box(0.8, 11, 1.25, 7.25, 11.5, 7.5), box(0.7, 11, 1.2, 7.3, 11.25, 7.55), box(9.25, 13, 1.75, 14.8, 13.5, 7),
						box(8.25, 9, 0.75, 15.75, 10, 8), box(8.3, 10, 0.8, 15.7, 10.25, 7.95), box(8.65, 14.9, 1.05, 15.4, 15.8, 7.6), box(9.1, 13.5, 1.55, 14.95, 14, 7.2), box(9.25, 12.5, 1.75, 14.8, 13, 7), box(8.7, 14.7, 1.2, 15.3, 14.95, 7.55),
						box(8.4, 10.25, 0.9, 15.6, 10.5, 7.85), box(8.5, 10.5, 1, 15.5, 10.75, 7.75), box(8.6, 10.75, 1.1, 15.4, 11, 7.65), box(8.8, 14.3, 1.25, 15.25, 14.8, 7.5), box(8.9, 14, 1.35, 15.15, 14.5, 7.4),
						box(9.1, 12, 1.55, 14.95, 12.5, 7.2), box(8.9, 11.5, 1.35, 15.15, 12, 7.4), box(8.8, 11, 1.25, 15.25, 11.5, 7.5), box(8.7, 11, 1.2, 15.3, 11.25, 7.55), box(0, 0, 0, 16, 16, 16));
			};
		}, ON, BURN);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return shapes.apply(state);
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
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, ON, BURN);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ON, false).setValue(BURN, 0);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
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