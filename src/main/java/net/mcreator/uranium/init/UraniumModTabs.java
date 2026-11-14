/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.uranium.UraniumMod;

public class UraniumModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UraniumMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> URANIUMCREATIVETAB = REGISTRY.register("uraniumcreativetab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.uranium.uraniumcreativetab")).icon(() -> new ItemStack(UraniumModItems.ENRICHEDURANIUM.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UraniumModItems.STEEL.get());
				tabData.accept(UraniumModBlocks.CENTRIFUGE.get().asItem());
				tabData.accept(UraniumModItems.DEPLETEDURANIUM.get());
				tabData.accept(UraniumModItems.ENRICHEDURANIUM.get());
				tabData.accept(UraniumModItems.YELLOWCAKE.get());
				tabData.accept(UraniumModItems.SULFURICACID_BUCKET.get());
				tabData.accept(UraniumModItems.FLUORIDE.get());
				tabData.accept(UraniumModItems.LINGOTDEPLOMB.get());
				tabData.accept(UraniumModItems.PEPITEDEPLOMB.get());
				tabData.accept(UraniumModBlocks.BLOCKDEPLOMB.get().asItem());
				tabData.accept(UraniumModBlocks.MINERAIDEPLOMB.get().asItem());
				tabData.accept(UraniumModBlocks.GROSSEBATTERIE.get().asItem());
				tabData.accept(UraniumModBlocks.SOLARPANEL.get().asItem());
				tabData.accept(UraniumModBlocks.CABLE.get().asItem());
				tabData.accept(UraniumModItems.URANIUMPOWDER.get());
				tabData.accept(UraniumModItems.ENRICHEDURANIUMINGOT.get());
				tabData.accept(UraniumModItems.DEPLETEDURANIUMINGOT.get());
				tabData.accept(UraniumModItems.FUELPELLET.get());
				tabData.accept(UraniumModItems.FUELROD.get());
				tabData.accept(UraniumModItems.SPENTFUELROD.get());
				tabData.accept(UraniumModItems.DEPLETEDURANIUMPLATE.get());
				tabData.accept(UraniumModItems.DEPLETEDURANIUMROUND.get());
				tabData.accept(UraniumModBlocks.CRUSHER.get().asItem());
				tabData.accept(UraniumModBlocks.REACTOR.get().asItem());
			}).build());
}