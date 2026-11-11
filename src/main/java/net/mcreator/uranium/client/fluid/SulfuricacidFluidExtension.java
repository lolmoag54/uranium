package net.mcreator.uranium.client.fluid;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.resources.Identifier;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.renderer.block.FluidModel;

import net.mcreator.uranium.init.UraniumModFluids;
import net.mcreator.uranium.init.UraniumModFluidTypes;

@EventBusSubscriber(Dist.CLIENT)
public class SulfuricacidFluidExtension {
	@SubscribeEvent
	public static void registerRegisterFluidModels(RegisterFluidModelsEvent event) {
		event.register(new FluidModel.Unbaked(new Material(Identifier.parse("uranium:block/water_still")), new Material(Identifier.parse("uranium:block/water_flow")), null, null), UraniumModFluids.SULFURICACID, UraniumModFluids.FLOWING_SULFURICACID);
	}

	@SubscribeEvent
	public static void registerFluidTypeExtensions(RegisterClientExtensionsEvent event) {
		event.registerFluidType(new IClientFluidTypeExtensions() {
		}, UraniumModFluidTypes.SULFURICACID_TYPE);
	}
}