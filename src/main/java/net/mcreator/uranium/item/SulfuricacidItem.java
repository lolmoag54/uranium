package net.mcreator.uranium.item;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

import net.mcreator.uranium.init.UraniumModFluids;

public class SulfuricacidItem extends BucketItem {
	public SulfuricacidItem(Item.Properties properties) {
		super(UraniumModFluids.SULFURICACID.get(), properties.craftRemainder(Items.BUCKET).stacksTo(1)

		);
	}
}