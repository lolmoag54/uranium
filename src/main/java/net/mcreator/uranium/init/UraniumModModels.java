/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.client.model.Modelpoisson_chat;
import net.mcreator.uranium.client.model.Modeleolienne;

@EventBusSubscriber(Dist.CLIENT)
public class UraniumModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelpoisson_chat.LAYER_LOCATION, Modelpoisson_chat::createBodyLayer);
		event.registerLayerDefinition(Modeleolienne.LAYER_LOCATION, Modeleolienne::createBodyLayer);
	}
}