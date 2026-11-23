/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.uranium.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.client.gui.*;

@EventBusSubscriber(Dist.CLIENT)
public class UraniumModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(UraniumModMenus.CENTRIFUGEGUI.get(), CentrifugeguiScreen::new);
		event.register(UraniumModMenus.CRUSHERGUI.get(), CrusherguiScreen::new);
		event.register(UraniumModMenus.REACTORGUI.get(), ReactorguiScreen::new);
		event.register(UraniumModMenus.LEADCRAFTINGTABLE_GUI.get(), LeadcraftingtableGuiScreen::new);
		event.register(UraniumModMenus.REACTORCONTROLGUI.get(), ReactorcontrolguiScreen::new);
		event.register(UraniumModMenus.COOLINGPOOLGUI.get(), CoolingpoolguiScreen::new);
		event.register(UraniumModMenus.REPROCESSINGPLANTGUI.get(), ReprocessingplantguiScreen::new);
		event.register(UraniumModMenus.CALCINATIONFURNACEGUI.get(), CalcinationfurnaceguiScreen::new);
		event.register(UraniumModMenus.SPLITTERGUI.get(), SplitterguiScreen::new);
		event.register(UraniumModMenus.CRYSTALLIZERGUI.get(), CrystallizerguiScreen::new);
		event.register(UraniumModMenus.CIPUGUI.get(), CipuguiScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}