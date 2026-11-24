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
				tabData.accept(UraniumModBlocks.RAWURANIUM.get().asItem());
				tabData.accept(UraniumModItems.FLUORIDE.get());
				tabData.accept(UraniumModItems.LINGOTDEPLOMB.get());
				tabData.accept(UraniumModItems.PEPITEDEPLOMB.get());
				tabData.accept(UraniumModBlocks.BLOCKDEPLOMB.get().asItem());
				tabData.accept(UraniumModBlocks.GROSSEBATTERIE.get().asItem());
				tabData.accept(UraniumModBlocks.SOLARPANEL.get().asItem());
				tabData.accept(UraniumModBlocks.CABLE.get().asItem());
				tabData.accept(UraniumModBlocks.RAWURANIUM_DEEPSLATE.get().asItem());
				tabData.accept(UraniumModItems.FUELROD.get());
				tabData.accept(UraniumModItems.SPENTFUELROD.get());
				tabData.accept(UraniumModBlocks.CRUSHER.get().asItem());
				tabData.accept(UraniumModBlocks.REACTOR.get().asItem());
				tabData.accept(UraniumModItems.RADIOACTIVEWASTELAND.get());
				tabData.accept(UraniumModBlocks.FLUORITE_ORE.get().asItem());
				tabData.accept(UraniumModBlocks.DEEPSLATE_FLUORITE_ORE.get().asItem());
				tabData.accept(UraniumModItems.URANIUMHEXAFLUORIDE.get());
				tabData.accept(UraniumModItems.FLUOR.get());
				tabData.accept(UraniumModItems.STEEL_HAMMER.get());
				tabData.accept(UraniumModItems.URANIUMPELLET.get());
				tabData.accept(UraniumModItems.ZICRONIUM.get());
				tabData.accept(UraniumModItems.ZICRON.get());
				tabData.accept(UraniumModBlocks.REINFORCEDCONCRETE.get().asItem());
				tabData.accept(UraniumModBlocks.LEADORE.get().asItem());
				tabData.accept(UraniumModItems.URANIUM_ARMOR_HELMET.get());
				tabData.accept(UraniumModItems.URANIUM_ARMOR_CHESTPLATE.get());
				tabData.accept(UraniumModItems.URANIUM_ARMOR_LEGGINGS.get());
				tabData.accept(UraniumModItems.URANIUM_ARMOR_BOOTS.get());
				tabData.accept(UraniumModItems.INSULATEDURANIUM.get());
				tabData.accept(UraniumModItems.ENRICHED_URANIUM_ARMOR_ARMOR_HELMET.get());
				tabData.accept(UraniumModItems.ENRICHED_URANIUM_ARMOR_ARMOR_CHESTPLATE.get());
				tabData.accept(UraniumModItems.ENRICHED_URANIUM_ARMOR_ARMOR_LEGGINGS.get());
				tabData.accept(UraniumModItems.ENRICHED_URANIUM_ARMOR_ARMOR_BOOTS.get());
				tabData.accept(UraniumModBlocks.WINDTURBINE.get().asItem());
				tabData.accept(UraniumModBlocks.COOLANTPIPE.get().asItem());
				tabData.accept(UraniumModBlocks.WATERPUMP.get().asItem());
				tabData.accept(UraniumModItems.PROSESSOR.get());
			}).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POLONIUM = REGISTRY.register("polonium",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.uranium.polonium")).icon(() -> new ItemStack(UraniumModItems.PLUTONIUMCORE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UraniumModItems.COOLEDSPENTFUELROD.get());
				tabData.accept(UraniumModItems.NITRICACID.get());
				tabData.accept(UraniumModItems.TBPSOLVENT.get());
				tabData.accept(UraniumModItems.OXALICACID.get());
				tabData.accept(UraniumModItems.PLUTONIUMNITRATE.get());
				tabData.accept(UraniumModItems.FISSIONWASTE.get());
				tabData.accept(UraniumModItems.PLUTONIUMDIOXIDE.get());
				tabData.accept(UraniumModItems.MOXFUELROD.get());
				tabData.accept(UraniumModItems.PLUTONIUMCORE.get());
				tabData.accept(UraniumModBlocks.COOLINGPOOL.get().asItem());
				tabData.accept(UraniumModBlocks.REPROCESSINGPLANT.get().asItem());
				tabData.accept(UraniumModBlocks.CALCINATIONFURNACE.get().asItem());
			}).withTabsBefore(URANIUMCREATIVETAB.getId()).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RADIUMCREATIVETAB = REGISTRY.register("radiumcreativetab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.uranium.radiumcreativetab")).icon(() -> new ItemStack(UraniumModItems.PENCHBLENDE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(UraniumModItems.PENCHBLENDE.get());
				tabData.accept(UraniumModBlocks.PENCHBLENDEORE.get().asItem());
				tabData.accept(UraniumModBlocks.SPLITTER.get().asItem());
				tabData.accept(UraniumModItems.PITCHBLENDECONCENTRATE.get());
				tabData.accept(UraniumModItems.POWDEREDFERTILIZEDSOIL.get());
				tabData.accept(UraniumModItems.ROCHESTERIL.get());
				tabData.accept(UraniumModBlocks.CLOSEDINDUSTRIALPROCESSINGUNIT.get().asItem());
				tabData.accept(UraniumModBlocks.CRYSTALLIZER.get().asItem());
				tabData.accept(UraniumModItems.RADIFEROUSCRYSTAL.get());
				tabData.accept(UraniumModItems.EMPTYVIAL.get());
				tabData.accept(UraniumModItems.WATERVIAL.get());
			}).withTabsBefore(POLONIUM.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(UraniumModItems.POISSONCHATTE_SPAWN_EGG.get());
			tabData.accept(UraniumModItems.CRYSTALSTAGSPAWNEGG.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(UraniumModItems.URANIUM_PICKAXE.get());
			tabData.accept(UraniumModItems.URANIUM_AXE.get());
			tabData.accept(UraniumModItems.URANIUM_SHOVEL.get());
			tabData.accept(UraniumModItems.URANIUM_HOE.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(UraniumModItems.URANIUM_SWORD.get());
		}
	}
}