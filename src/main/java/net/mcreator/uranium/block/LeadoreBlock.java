package net.mcreator.uranium.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class LeadoreBlock extends Block {
	public LeadoreBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.METAL).strength(1f, 10f).requiresCorrectToolForDrops());
	}
}