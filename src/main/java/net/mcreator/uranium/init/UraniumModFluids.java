/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.uranium.fluid.SulfuricacidFluid;
import net.mcreator.uranium.UraniumMod;

public class UraniumModFluids {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(BuiltInRegistries.FLUID, UraniumMod.MODID);
	public static final DeferredHolder<Fluid, FlowingFluid> SULFURICACID = REGISTRY.register("sulfuricacid", SulfuricacidFluid.Source::new);
	public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_SULFURICACID = REGISTRY.register("flowing_sulfuricacid", SulfuricacidFluid.Flowing::new);
}