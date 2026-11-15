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

import net.mcreator.uranium.world.inventory.CentrifugeguiMenu;
import net.mcreator.uranium.procedures.CentrifugeOnTickUpdateProcedure;
import net.mcreator.uranium.procedures.CentrifugeBlockAddedProcedure;
import net.mcreator.uranium.block.entity.CentrifugeBlockEntity;

import java.util.function.Function;

import io.netty.buffer.Unpooled;

import com.mojang.serialization.MapCodec;

public class CentrifugeBlock extends FallingBlock implements EntityBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final IntegerProperty CENTRIFUGATION = IntegerProperty.create("centrifugation", 0, 400);
	private final Function<BlockState, VoxelShape> shapes = this.makeShapes();
	public static final MapCodec<CentrifugeBlock> CODEC = simpleCodec(CentrifugeBlock::new);

	@Override
	public MapCodec<CentrifugeBlock> codec() {
		return CODEC;
	}

	@Override
	public int getDustColor(BlockState blockstate, BlockGetter world, BlockPos pos) {
		return blockstate.getMapColor(world, pos).col;
	}

	public CentrifugeBlock(BlockBehaviour.Properties properties) {
		super(properties
				.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.break")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.step")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.place")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.hit")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.anvil.fall"))))
				.strength(1f, 10f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false).instrument(NoteBlockInstrument.IRON_XYLOPHONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ON, false).setValue(CENTRIFUGATION, 0));
	}

	private Function<BlockState, VoxelShape> makeShapes() {
		return this.getShapeForEachState(state -> {
			return switch (state.getValue(FACING)) {
				case NORTH -> Shapes.or(box(6, 1, 6, 10, 2, 10), box(6, 19, 6, 10, 21, 10), box(6, 3, 6, 10, 19, 10), box(2, 0, 3, 4, 2, 5), box(3, 1, 4, 13, 2, 5), box(4, 2, 5, 12, 3, 11), box(3, 18, 4, 13, 19, 12), box(3, 1, 11, 13, 2, 12),
						box(12, 0, 3, 14, 2, 5), box(2, 0, 11, 4, 2, 13), box(12, 0, 11, 14, 2, 13), box(3, 1, 5, 4, 2, 11), box(12, 1, 5, 13, 2, 11), box(1, 20, 7, 6, 21, 8), box(10, 20, 7, 11, 23, 8), box(1, 2, 7, 2, 20, 8), box(2, 2, 7, 5, 3, 8));
				case EAST -> Shapes.or(box(6, 1, 6, 10, 2, 10), box(6, 19, 6, 10, 21, 10), box(6, 3, 6, 10, 19, 10), box(11, 0, 2, 13, 2, 4), box(11, 1, 3, 12, 2, 13), box(5, 2, 4, 11, 3, 12), box(4, 18, 3, 12, 19, 13), box(4, 1, 3, 5, 2, 13),
						box(11, 0, 12, 13, 2, 14), box(3, 0, 2, 5, 2, 4), box(3, 0, 12, 5, 2, 14), box(5, 1, 3, 11, 2, 4), box(5, 1, 12, 11, 2, 13), box(8, 20, 1, 9, 21, 6), box(8, 20, 10, 9, 23, 11), box(8, 2, 1, 9, 20, 2), box(8, 2, 2, 9, 3, 5));
				case WEST -> Shapes.or(box(6, 1, 6, 10, 2, 10), box(6, 19, 6, 10, 21, 10), box(6, 3, 6, 10, 19, 10), box(3, 0, 12, 5, 2, 14), box(4, 1, 3, 5, 2, 13), box(5, 2, 4, 11, 3, 12), box(4, 18, 3, 12, 19, 13), box(11, 1, 3, 12, 2, 13),
						box(3, 0, 2, 5, 2, 4), box(11, 0, 12, 13, 2, 14), box(11, 0, 2, 13, 2, 4), box(5, 1, 12, 11, 2, 13), box(5, 1, 3, 11, 2, 4), box(7, 20, 10, 8, 21, 15), box(7, 20, 5, 8, 23, 6), box(7, 2, 14, 8, 20, 15),
						box(7, 2, 11, 8, 3, 14));
				default -> Shapes.or(box(6, 1, 6, 10, 2, 10), box(6, 19, 6, 10, 21, 10), box(6, 3, 6, 10, 19, 10), box(12, 0, 11, 14, 2, 13), box(3, 1, 11, 13, 2, 12), box(4, 2, 5, 12, 3, 11), box(3, 18, 4, 13, 19, 12), box(3, 1, 4, 13, 2, 5),
						box(2, 0, 11, 4, 2, 13), box(12, 0, 3, 14, 2, 5), box(2, 0, 3, 4, 2, 5), box(12, 1, 5, 13, 2, 11), box(3, 1, 5, 4, 2, 11), box(10, 20, 8, 15, 21, 9), box(5, 20, 8, 6, 23, 9), box(14, 2, 8, 15, 20, 9), box(11, 2, 8, 14, 3, 9));
			};
		}, CENTRIFUGATION);
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
		builder.add(FACING, ON, CENTRIFUGATION);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ON, false).setValue(CENTRIFUGATION, 0);
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
		CentrifugeBlockAddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		CentrifugeOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), blockstate);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		if (entity instanceof ServerPlayer player) {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("Centrifuge");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new CentrifugeguiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
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
		return new CentrifugeBlockEntity(pos, state);
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
		if (tileentity instanceof CentrifugeBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}