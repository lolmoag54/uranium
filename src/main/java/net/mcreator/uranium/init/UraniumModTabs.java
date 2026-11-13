/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.uranium.UraniumMod;

@EventBusSubscriber
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
				tabData.accept(UraniumModBlocks.SOLARPANEL.get().asItem());
				tabData.accept(UraniumModBlocks.CABLE.get().asItem());
			}).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(UraniumModItems.LINGOTDEPLOMB.get());
			tabData.accept(UraniumModItems.PEPITEDEPLOMB.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(UraniumModBlocks.BLOCKDEPLOMB.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(UraniumModBlocks.MINERAIDEPLOMB.get().asItem());
		}
	}
}