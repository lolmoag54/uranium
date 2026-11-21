package net.mcreator.uranium.init;

import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.uranium.item.*;
import net.mcreator.uranium.UraniumMod;

import java.util.function.Function;

@EventBusSubscriber
public class UraniumModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(UraniumMod.MODID);
	public static final DeferredItem<Item> STEEL;
	public static final DeferredItem<Item> CENTRIFUGE;
	public static final DeferredItem<Item> DEPLETEDURANIUM;
	public static final DeferredItem<Item> ENRICHEDURANIUM;
	public static final DeferredItem<Item> YELLOWCAKE;
	public static final DeferredItem<Item> SULFURICACID_BUCKET;
	public static final DeferredItem<Item> RAWURANIUM;
	public static final DeferredItem<Item> FLUORIDE;
	public static final DeferredItem<Item> LINGOTDEPLOMB;
	public static final DeferredItem<Item> PEPITEDEPLOMB;
	public static final DeferredItem<Item> BLOCKDEPLOMB;
	public static final DeferredItem<Item> GROSSEBATTERIE;
	public static final DeferredItem<Item> SOLARPANEL;
	public static final DeferredItem<Item> CABLE;
	public static final DeferredItem<Item> CABLE_E;
	public static final DeferredItem<Item> CABLE_I;
	public static final DeferredItem<Item> CABLE_L;
	public static final DeferredItem<Item> CABLE_LC;
	public static final DeferredItem<Item> CABLE_LCC;
	public static final DeferredItem<Item> CABLE_LT;
	public static final DeferredItem<Item> CABLE_LTC;
	public static final DeferredItem<Item> CABLE_T;
	public static final DeferredItem<Item> CABLE_TC;
	public static final DeferredItem<Item> CABLE_TX;
	public static final DeferredItem<Item> CABLE_TXC;
	public static final DeferredItem<Item> CABLE_TXCC;
	public static final DeferredItem<Item> CABLE_X;
	public static final DeferredItem<Item> CABLE_XC;
	public static final DeferredItem<Item> CABLE_F;
	public static final DeferredItem<Item> CABLE_S;
	public static final DeferredItem<Item> RAWURANIUM_DEEPSLATE;
	public static final DeferredItem<Item> FUELROD;
	public static final DeferredItem<Item> SPENTFUELROD;
	public static final DeferredItem<Item> CRUSHER;
	public static final DeferredItem<Item> REACTOR;
	public static final DeferredItem<Item> POISSONCHATTE_SPAWN_EGG;
	public static final DeferredItem<Item> CRUCHED_RAWURANIUM;
	public static final DeferredItem<Item> RADIOACTIVEWASTELAND;
	public static final DeferredItem<Item> FLUORITE_ORE;
	public static final DeferredItem<Item> DEEPSLATE_FLUORITE_ORE;
	public static final DeferredItem<Item> URANIUMHEXAFLUORIDE;
	public static final DeferredItem<Item> FLUOR;
	public static final DeferredItem<Item> STEEL_HAMMER;
	public static final DeferredItem<Item> URANIUMPELLET;
	public static final DeferredItem<Item> ZICRONIUM;
	public static final DeferredItem<Item> ZICRON;
	public static final DeferredItem<Item> ZICRON_ORE;
	public static final DeferredItem<Item> REINFORCEDCONCRETE;
	public static final DeferredItem<Item> LEADCRAFTINGTABLE;
	public static final DeferredItem<Item> LEADORE;
	public static final DeferredItem<Item> URANIUM_PICKAXE;
	public static final DeferredItem<Item> URANIUM_AXE;
	public static final DeferredItem<Item> URANIUM_SWORD;
	public static final DeferredItem<Item> URANIUM_SHOVEL;
	public static final DeferredItem<Item> URANIUM_HOE;
	public static final DeferredItem<Item> URANIUM_ARMOR_HELMET;
	public static final DeferredItem<Item> URANIUM_ARMOR_CHESTPLATE;
	public static final DeferredItem<Item> URANIUM_ARMOR_LEGGINGS;
	public static final DeferredItem<Item> URANIUM_ARMOR_BOOTS;
	public static final DeferredItem<Item> INSULATEDURANIUM;
	public static final DeferredItem<Item> ENRICHED_URANIUM_ARMOR_ARMOR_HELMET;
	public static final DeferredItem<Item> ENRICHED_URANIUM_ARMOR_ARMOR_CHESTPLATE;
	public static final DeferredItem<Item> ENRICHED_URANIUM_ARMOR_ARMOR_LEGGINGS;
	public static final DeferredItem<Item> ENRICHED_URANIUM_ARMOR_ARMOR_BOOTS;
	public static final DeferredItem<Item> CRYSTALSTAGSPAWNEGG;
	public static final DeferredItem<Item> WINDTURBINE;
	static {
		STEEL = register("steel", SteelItem::new);
		CENTRIFUGE = block(UraniumModBlocks.CENTRIFUGE, new Item.Properties().stacksTo(16));
		DEPLETEDURANIUM = register("depleteduranium", DepleteduraniumItem::new);
		ENRICHEDURANIUM = register("enricheduranium", EnricheduraniumItem::new);
		YELLOWCAKE = register("yellowcake", YellowcakeItem::new);
		SULFURICACID_BUCKET = register("sulfuricacid_bucket", SulfuricacidItem::new);
		RAWURANIUM = block(UraniumModBlocks.RAWURANIUM);
		FLUORIDE = register("fluoride", FluorideItem::new);
		LINGOTDEPLOMB = register("lingotdeplomb", LingotdeplombItem::new);
		PEPITEDEPLOMB = register("pepitedeplomb", PepitedeplombItem::new);
		BLOCKDEPLOMB = block(UraniumModBlocks.BLOCKDEPLOMB);
		GROSSEBATTERIE = block(UraniumModBlocks.GROSSEBATTERIE);
		SOLARPANEL = block(UraniumModBlocks.SOLARPANEL);
		CABLE = block(UraniumModBlocks.CABLE);
		CABLE_E = block(UraniumModBlocks.CABLE_E);
		CABLE_I = block(UraniumModBlocks.CABLE_I);
		CABLE_L = block(UraniumModBlocks.CABLE_L);
		CABLE_LC = block(UraniumModBlocks.CABLE_LC);
		CABLE_LCC = block(UraniumModBlocks.CABLE_LCC);
		CABLE_LT = block(UraniumModBlocks.CABLE_LT);
		CABLE_LTC = block(UraniumModBlocks.CABLE_LTC);
		CABLE_T = block(UraniumModBlocks.CABLE_T);
		CABLE_TC = block(UraniumModBlocks.CABLE_TC);
		CABLE_TX = block(UraniumModBlocks.CABLE_TX);
		CABLE_TXC = block(UraniumModBlocks.CABLE_TXC);
		CABLE_TXCC = block(UraniumModBlocks.CABLE_TXCC);
		CABLE_X = block(UraniumModBlocks.CABLE_X);
		CABLE_XC = block(UraniumModBlocks.CABLE_XC);
		CABLE_F = block(UraniumModBlocks.CABLE_F);
		CABLE_S = block(UraniumModBlocks.CABLE_S);
		RAWURANIUM_DEEPSLATE = block(UraniumModBlocks.RAWURANIUM_DEEPSLATE);
		FUELROD = register("fuelrod", FuelrodItem::new);
		SPENTFUELROD = register("spentfuelrod", SpentfuelrodItem::new);
		CRUSHER = block(UraniumModBlocks.CRUSHER, new Item.Properties().stacksTo(16));
		REACTOR = block(UraniumModBlocks.REACTOR, new Item.Properties().stacksTo(16));
		POISSONCHATTE_SPAWN_EGG = register("poissonchatte_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(UraniumModEntities.POISSONCHATTE.get())));
		CRUCHED_RAWURANIUM = register("cruched_rawuranium", CruchedRawuraniumItem::new);
		RADIOACTIVEWASTELAND = register("radioactivewasteland", RadioactivewastelandItem::new);
		FLUORITE_ORE = block(UraniumModBlocks.FLUORITE_ORE);
		DEEPSLATE_FLUORITE_ORE = block(UraniumModBlocks.DEEPSLATE_FLUORITE_ORE);
		URANIUMHEXAFLUORIDE = register("uraniumhexafluoride", UraniumhexafluorideItem::new);
		FLUOR = register("fluor", FluorItem::new);
		STEEL_HAMMER = register("steel_hammer", SteelHammerItem::new);
		URANIUMPELLET = register("uraniumpellet", UraniumpelletItem::new);
		ZICRONIUM = register("zicronium", ZicroniumItem::new);
		ZICRON = register("zicron", ZicronItem::new);
		ZICRON_ORE = block(UraniumModBlocks.ZICRON_ORE);
		REINFORCEDCONCRETE = block(UraniumModBlocks.REINFORCEDCONCRETE);
		LEADCRAFTINGTABLE = block(UraniumModBlocks.LEADCRAFTINGTABLE);
		LEADORE = block(UraniumModBlocks.LEADORE);
		URANIUM_PICKAXE = register("uranium_pickaxe", UraniumPickaxeItem::new);
		URANIUM_AXE = register("uranium_axe", UraniumAxeItem::new);
		URANIUM_SWORD = register("uranium_sword", UraniumSwordItem::new);
		URANIUM_SHOVEL = register("uranium_shovel", UraniumShovelItem::new);
		URANIUM_HOE = register("uranium_hoe", UraniumHoeItem::new);
		URANIUM_ARMOR_HELMET = register("uranium_armor_helmet", UraniumArmorItem.Helmet::new);
		URANIUM_ARMOR_CHESTPLATE = register("uranium_armor_chestplate", UraniumArmorItem.Chestplate::new);
		URANIUM_ARMOR_LEGGINGS = register("uranium_armor_leggings", UraniumArmorItem.Leggings::new);
		URANIUM_ARMOR_BOOTS = register("uranium_armor_boots", UraniumArmorItem.Boots::new);
		INSULATEDURANIUM = register("insulateduranium", InsulateduraniumItem::new);
		ENRICHED_URANIUM_ARMOR_ARMOR_HELMET = register("enriched_uranium_armor_armor_helmet", Enriched_uranium_armorArmorItem.Helmet::new);
		ENRICHED_URANIUM_ARMOR_ARMOR_CHESTPLATE = register("enriched_uranium_armor_armor_chestplate", Enriched_uranium_armorArmorItem.Chestplate::new);
		ENRICHED_URANIUM_ARMOR_ARMOR_LEGGINGS = register("enriched_uranium_armor_armor_leggings", Enriched_uranium_armorArmorItem.Leggings::new);
		ENRICHED_URANIUM_ARMOR_ARMOR_BOOTS = register("enriched_uranium_armor_armor_boots", Enriched_uranium_armorArmorItem.Boots::new);
		CRYSTALSTAGSPAWNEGG = register("crystalstagspawnegg", CrystalstagspawneggItem::new);
		WINDTURBINE = block(UraniumModBlocks.WINDTURBINE);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, Item.Properties::new);
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), () -> properties);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new BucketResourceHandler(access), SULFURICACID_BUCKET.get());
	}
}