package net.mcreator.uranium.block.model;

import net.minecraft.resources.Identifier;

import net.mcreator.uranium.block.entity.WindturbineTileEntity;

import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.model.DefaultedBlockGeoModel;
import com.geckolib.cache.GeckoLibResources;

public class WindturbineBlockModel extends DefaultedBlockGeoModel<WindturbineTileEntity> {
	/** Flat layout: geckolib/models/<name>.geo.json */
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("uranium", "geckolib_block");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("uranium", "geckolib_block");
	/** Importer default: geckolib/models/entity/<name>.geo.json */
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("uranium", "entity/geckolib_block");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("uranium", "entity/geckolib_block");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("uranium", "textures/block/eolienne.png");

	public WindturbineBlockModel() {
		// DefaultedBlockGeoModel prefixes model/anim keys with block/
		// matching: geckolib/models/block/<path> and geckolib/animations/block/<path>
		super(Identifier.fromNamespaceAndPath("uranium", "geckolib_block"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		Identifier preferred = super.getModelResource(renderState);
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		return MODEL_ENTITY;
	}

	@Override
	public Identifier getAnimationResource(WindturbineTileEntity animatable) {
		Identifier preferred = super.getAnimationResource(animatable);
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		return ANIM_ENTITY;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(WindturbineTileEntity animatable) {
		return new Identifier[]{ANIM_FLAT, ANIM_ENTITY};
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		// MCreator block textures: assets/<modid>/textures/block/<name>.png
		return TEXTURE;
	}
}