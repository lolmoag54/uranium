package net.mcreator.uranium.entity.layer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import net.mcreator.uranium.entity.CrystalstagEntity;

import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import com.geckolib.renderer.base.GeoRenderer;

public class CrystalstagLayer extends AutoGlowingGeoLayer<CrystalstagEntity, Void, LivingEntityRenderState> {
	private static final Identifier GLOW = Identifier.fromNamespaceAndPath("uranium", "textures/entities/cerf_cristallin_glowmask.png");

	public CrystalstagLayer(GeoRenderer<CrystalstagEntity, Void, LivingEntityRenderState> renderer) {
		super(renderer);
	}

	@Override
	protected Identifier getTextureResource(LivingEntityRenderState renderState) {
		return GLOW;
	}
}