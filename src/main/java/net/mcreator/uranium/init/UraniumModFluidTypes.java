/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.fluids.FluidType;

import net.mcreator.uranium.fluid.types.SulfuricacidFluidType;
import net.mcreator.uranium.UraniumMod;

public class UraniumModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, UraniumMod.MODID);
	public static final DeferredHolder<FluidType, FluidType> SULFURICACID_TYPE = REGISTRY.register("sulfuricacid", SulfuricacidFluidType::new);
}