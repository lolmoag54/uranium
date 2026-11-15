package net.mcreator.uranium.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class FluoriteOreBlock extends Block {
	public FluoriteOreBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f));
	}
}