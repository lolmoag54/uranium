package net.mcreator.uranium.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class ClosedindustrialprocessingunitBlock extends Block {
	public ClosedindustrialprocessingunitBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.ANVIL).strength(1f, 10f).requiresCorrectToolForDrops());
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	public int getLightDampening(BlockState state) {
		return 0;
	}
}