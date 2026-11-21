package net.mcreator.uranium.block.listener;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.init.UraniumModBlockEntities;
import net.mcreator.uranium.block.renderer.WindturbineTileRenderer;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientListener {
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(UraniumModBlockEntities.WINDTURBINE.get(), WindturbineTileRenderer::new);
	}
}