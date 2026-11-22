package net.mcreator.uranium.block.model;

import net.minecraft.resources.Identifier;

import net.mcreator.uranium.block.display.WindturbineDisplayItem;

import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.model.DefaultedBlockGeoModel;
import com.geckolib.cache.GeckoLibResources;

public class WindturbineDisplayModel extends DefaultedBlockGeoModel<WindturbineDisplayItem> {
	/** Preferred: geckolib/models/block/<name> (from DefaultedBlockGeoModel super) */
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("uranium", "geckolib_block");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("uranium", "geckolib_block");
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("uranium", "entity/geckolib_block");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("uranium", "entity/geckolib_block");
	private static final Identifier MODEL_ITEM = Identifier.fromNamespaceAndPath("uranium", "item/geckolib_block");
	private static final Identifier ANIM_ITEM = Identifier.fromNamespaceAndPath("uranium", "item/geckolib_block");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("uranium", "textures/block/eolienne.png");

	public WindturbineDisplayModel() {
		// Same block/ asset prefix as the block entity model
		super(Identifier.fromNamespaceAndPath("uranium", "geckolib_block"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		Identifier preferred = super.getModelResource(renderState); // modid:block/<name>
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_ITEM;
	}

	@Override
	public Identifier getAnimationResource(WindturbineDisplayItem animatable) {
		Identifier preferred = super.getAnimationResource(animatable); // modid:block/<name>
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_ITEM;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(WindturbineDisplayItem animatable) {
		return new Identifier[]{ANIM_FLAT, ANIM_ENTITY, ANIM_ITEM};
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		// Display items use the block texture (MCreator textures/block/)
		return TEXTURE;
	}
}