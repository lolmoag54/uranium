package net.mcreator.uranium.fluid;

import org.apache.logging.log4j.core.util.Source;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;

import net.mcreator.uranium.init.UraniumModItems;
import net.mcreator.uranium.init.UraniumModFluids;
import net.mcreator.uranium.init.UraniumModFluidTypes;
import net.mcreator.uranium.init.UraniumModBlocks;

public abstract class SulfuricacidFluid extends BaseFlowingFluid {
	public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(() -> UraniumModFluidTypes.SULFURICACID_TYPE.get(), () -> UraniumModFluids.SULFURICACID.get(), () -> UraniumModFluids.FLOWING_SULFURICACID.get())
			.explosionResistance(100f).slopeFindDistance(5).bucket(() -> UraniumModItems.SULFURICACID_BUCKET.get()).block(() -> (LiquidBlock) UraniumModBlocks.SULFURICACID.get());

	private SulfuricacidFluid() {
		super(PROPERTIES);
	}

	@Override
	public ParticleOptions getDripParticle() {
		return ParticleTypes.ASH;
	}

	public static class Source extends SulfuricacidFluid {
		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends SulfuricacidFluid {
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}