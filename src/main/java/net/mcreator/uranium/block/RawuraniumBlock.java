package net.mcreator.uranium.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class RawuraniumBlock extends Block {
	public RawuraniumBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).instrument(NoteBlockInstrument.BELL));
	}
}