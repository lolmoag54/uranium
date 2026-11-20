package net.mcreator.uranium.client;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.init.UraniumModEntities;
import net.mcreator.uranium.client.renderer.CrystalstagRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class AnimatedEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(UraniumModEntities.CRYSTALSTAG.get(), CrystalstagRenderer::new);
	}
}