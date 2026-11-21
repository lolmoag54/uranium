package net.mcreator.uranium.init;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.uranium.entity.CrystalstagEntity;

@EventBusSubscriber(value = Dist.CLIENT)
public class EntityAnimationFactory {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		if (event == null || event.getEntity() == null)
			return;
		if (event.getEntity() instanceof CrystalstagEntity syncable) {
			String animation = syncable.getSyncedAnimation();
			if (!animation.equals("undefined")) {
				syncable.getEntityData().set(CrystalstagEntity.ANIMATION, "undefined");
				syncable.animationprocedure = animation;
			}
			syncable.applySyncedControllerAnimations();
		}
	}
}