package net.mcreator.uranium.init;

import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.uranium.block.entity.*;
import net.mcreator.uranium.UraniumMod;

@EventBusSubscriber
public class UraniumModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, UraniumMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE = register("centrifuge", UraniumModBlocks.CENTRIFUGE, CentrifugeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrossebatterieBlockEntity>> GROSSEBATTERIE = register("grossebatterie", UraniumModBlocks.GROSSEBATTERIE, GrossebatterieBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarpanelBlockEntity>> SOLARPANEL = register("solarpanel", UraniumModBlocks.SOLARPANEL, SolarpanelBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableBlockEntity>> CABLE = register("cable", UraniumModBlocks.CABLE, CableBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableEBlockEntity>> CABLE_E = register("cable_e", UraniumModBlocks.CABLE_E, CableEBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableIBlockEntity>> CABLE_I = register("cable_i", UraniumModBlocks.CABLE_I, CableIBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableLBlockEntity>> CABLE_L = register("cable_l", UraniumModBlocks.CABLE_L, CableLBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableLCBlockEntity>> CABLE_LC = register("cable_lc", UraniumModBlocks.CABLE_LC, CableLCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableLCCBlockEntity>> CABLE_LCC = register("cable_lcc", UraniumModBlocks.CABLE_LCC, CableLCCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableLTBlockEntity>> CABLE_LT = register("cable_lt", UraniumModBlocks.CABLE_LT, CableLTBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableLTCBlockEntity>> CABLE_LTC = register("cable_ltc", UraniumModBlocks.CABLE_LTC, CableLTCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableTBlockEntity>> CABLE_T = register("cable_t", UraniumModBlocks.CABLE_T, CableTBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableTCBlockEntity>> CABLE_TC = register("cable_tc", UraniumModBlocks.CABLE_TC, CableTCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableTXBlockEntity>> CABLE_TX = register("cable_tx", UraniumModBlocks.CABLE_TX, CableTXBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableTXCBlockEntity>> CABLE_TXC = register("cable_txc", UraniumModBlocks.CABLE_TXC, CableTXCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableTXCCBlockEntity>> CABLE_TXCC = register("cable_txcc", UraniumModBlocks.CABLE_TXCC, CableTXCCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableXBlockEntity>> CABLE_X = register("cable_x", UraniumModBlocks.CABLE_X, CableXBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableXCBlockEntity>> CABLE_XC = register("cable_xc", UraniumModBlocks.CABLE_XC, CableXCBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableFBlockEntity>> CABLE_F = register("cable_f", UraniumModBlocks.CABLE_F, CableFBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableSBlockEntity>> CABLE_S = register("cable_s", UraniumModBlocks.CABLE_S, CableSBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrusherBlockEntity>> CRUSHER = register("crusher", UraniumModBlocks.CRUSHER, CrusherBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReactorBlockEntity>> REACTOR = register("reactor", UraniumModBlocks.REACTOR, ReactorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LeadcraftingtableBlockEntity>> LEADCRAFTINGTABLE = register("leadcraftingtable", UraniumModBlocks.LEADCRAFTINGTABLE, LeadcraftingtableBlockEntity::new);

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> new BlockEntityType(supplier, block.get()));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Item.BLOCK, CENTRIFUGE.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CENTRIFUGE.get(), (blockEntity, side) -> ((CentrifugeBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, GROSSEBATTERIE.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, GROSSEBATTERIE.get(), (blockEntity, side) -> ((GrossebatterieBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, SOLARPANEL.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, SOLARPANEL.get(), (blockEntity, side) -> ((SolarpanelBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE.get(), (blockEntity, side) -> ((CableBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_E.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_E.get(), (blockEntity, side) -> ((CableEBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_I.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_I.get(), (blockEntity, side) -> ((CableIBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_L.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_L.get(), (blockEntity, side) -> ((CableLBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_LC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_LC.get(), (blockEntity, side) -> ((CableLCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_LCC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_LCC.get(), (blockEntity, side) -> ((CableLCCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_LT.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_LT.get(), (blockEntity, side) -> ((CableLTBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_LTC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_LTC.get(), (blockEntity, side) -> ((CableLTCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_T.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_T.get(), (blockEntity, side) -> ((CableTBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_TC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_TC.get(), (blockEntity, side) -> ((CableTCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_TX.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_TX.get(), (blockEntity, side) -> ((CableTXBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_TXC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_TXC.get(), (blockEntity, side) -> ((CableTXCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_TXCC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_TXCC.get(), (blockEntity, side) -> ((CableTXCCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_X.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_X.get(), (blockEntity, side) -> ((CableXBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_XC.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_XC.get(), (blockEntity, side) -> ((CableXCBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_F.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_F.get(), (blockEntity, side) -> ((CableFBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CABLE_S.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CABLE_S.get(), (blockEntity, side) -> ((CableSBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, CRUSHER.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, CRUSHER.get(), (blockEntity, side) -> ((CrusherBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, REACTOR.get(), WorldlyContainerWrapper::new);
		event.registerBlockEntity(Capabilities.Energy.BLOCK, REACTOR.get(), (blockEntity, side) -> ((ReactorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.Item.BLOCK, LEADCRAFTINGTABLE.get(), WorldlyContainerWrapper::new);
	}
}