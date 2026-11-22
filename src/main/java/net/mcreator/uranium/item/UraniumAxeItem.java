package net.mcreator.uranium.item;

import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.AxeItem;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

public class UraniumAxeItem extends AxeItem {
	private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3072, 18f, 0, 84, TagKey.create(Registries.ITEM, Identifier.parse("uranium:uranium_axe_repair_items")));

	public UraniumAxeItem(Item.Properties properties) {
		super(TOOL_MATERIAL, 23f, 14f, properties);
	}
}