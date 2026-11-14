/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.client.gui.ReactorguiScreen;
import net.mcreator.uranium.client.gui.CrusherguiScreen;
import net.mcreator.uranium.client.gui.CentrifugeguiScreen;

@EventBusSubscriber(Dist.CLIENT)
public class UraniumModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(UraniumModMenus.CENTRIFUGEGUI.get(), CentrifugeguiScreen::new);
		event.register(UraniumModMenus.CRUSHERGUI.get(), CrusherguiScreen::new);
		event.register(UraniumModMenus.REACTORGUI.get(), ReactorguiScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}