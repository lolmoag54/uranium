package net.mcreator.uranium.item;

import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.uranium.procedures.EnricheduraniumarmorArmorEvenementDeTickDuPlastronProcedure;
import net.mcreator.uranium.procedures.EnricheduraniumarmorArmorEvenementDeTickDuCasqueProcedure;
import net.mcreator.uranium.procedures.EnricheduraniumarmorArmorEvenementDeTickDesJambieresProcedure;
import net.mcreator.uranium.procedures.EnricheduraniumarmorArmorEvenementDeTickDesBottesProcedure;

import javax.annotation.Nullable;

import java.util.Map;

public abstract class Enriched_uranium_armorArmorItem extends Item {
	public static ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(90, Map.of(ArmorType.BOOTS, 12, ArmorType.LEGGINGS, 30, ArmorType.CHESTPLATE, 36, ArmorType.HELMET, 12, ArmorType.BODY, 36), 54,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY), 0f, 0f, TagKey.create(Registries.ITEM, Identifier.parse("uranium:enriched_uranium_armor_armor_repair_items")),
			ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("uranium:enriched_uranium_armor_armor")));

	private Enriched_uranium_armorArmorItem(Item.Properties properties) {
		super(properties);
	}

	public static class Helmet extends Enriched_uranium_armorArmorItem {
		public Helmet(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET));
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				EnricheduraniumarmorArmorEvenementDeTickDuCasqueProcedure.execute(entity);
			}
		}
	}

	public static class Chestplate extends Enriched_uranium_armorArmorItem {
		public Chestplate(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.CHESTPLATE));
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				EnricheduraniumarmorArmorEvenementDeTickDuPlastronProcedure.execute(entity);
			}
		}
	}

	public static class Leggings extends Enriched_uranium_armorArmorItem {
		public Leggings(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.LEGGINGS));
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				EnricheduraniumarmorArmorEvenementDeTickDesJambieresProcedure.execute(entity);
			}
		}
	}

	public static class Boots extends Enriched_uranium_armorArmorItem {
		public Boots(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.BOOTS));
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				EnricheduraniumarmorArmorEvenementDeTickDesBottesProcedure.execute(entity);
			}
		}
	}
}