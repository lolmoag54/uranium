package net.mcreator.uranium.block.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.mcreator.uranium.block.model.WindturbineBlockModel;
import net.mcreator.uranium.block.entity.WindturbineTileEntity;

import com.geckolib.renderer.GeoBlockRenderer;

public class WindturbineTileRenderer extends GeoBlockRenderer<WindturbineTileEntity, BlockEntityRenderState> {
	public WindturbineTileRenderer(BlockEntityRendererProvider.Context context) {
		super(context, new WindturbineBlockModel());
	}
}