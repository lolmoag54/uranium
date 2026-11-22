package net.mcreator.uranium.block.renderer;

import net.mcreator.uranium.block.model.WindturbineDisplayModel;
import net.mcreator.uranium.block.display.WindturbineDisplayItem;

import com.geckolib.renderer.GeoItemRenderer;

public class WindturbineDisplayItemRenderer extends GeoItemRenderer<WindturbineDisplayItem> {
	public WindturbineDisplayItemRenderer() {
		super(new WindturbineDisplayModel());
	}
}