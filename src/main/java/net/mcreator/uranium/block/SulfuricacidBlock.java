package net.mcreator.uranium.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LiquidBlock;

import net.mcreator.uranium.init.UraniumModFluids;

public class SulfuricacidBlock extends LiquidBlock {
	public SulfuricacidBlock(BlockBehaviour.Properties properties) {
		super(UraniumModFluids.SULFURICACID.get(), properties.mapColor(MapColor.WATER).strength(100f).noCollision().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}
}