package net.mcreator.uranium.item;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;

public class SteelHammerItem extends Item {
	public SteelHammerItem(Item.Properties properties) {
		super(properties.durability(100).setNoCombineRepair());
	}

	@Override
	public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
		ItemStack retval = new ItemStack(this);
		retval.setDamageValue(itemInstance.getOrDefault(DataComponents.DAMAGE, 0) + 1);
		if (retval.getDamageValue() >= retval.getMaxDamage()) {
			return null;
		}
		return ItemStackTemplate.fromNonEmptyStack(retval);
	}
}