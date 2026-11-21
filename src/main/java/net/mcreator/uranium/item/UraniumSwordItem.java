package net.mcreator.uranium.item;

import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

public class UraniumSwordItem extends Item {
	private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3072, 18f, 0, 84, TagKey.create(Registries.ITEM, Identifier.parse("uranium:uranium_sword_repair_items")));

	public UraniumSwordItem(Item.Properties properties) {
		super(properties.sword(TOOL_MATERIAL, 23f, 14f));
	}
}