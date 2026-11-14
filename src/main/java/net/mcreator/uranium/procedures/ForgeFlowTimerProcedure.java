package net.mcreator.uranium.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.uranium.network.UraniumModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ForgeFlowTimerProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!world.isClientSide()) {
			if (UraniumModVariables.MapVariables.get(world).forgeFlow == 0) {
				UraniumModVariables.MapVariables.get(world).forgeFlow = 300;
				UraniumModVariables.MapVariables.get(world).markSyncDirty();
			} else {
				UraniumModVariables.MapVariables.get(world).forgeFlow = UraniumModVariables.MapVariables.get(world).forgeFlow - 1;
				UraniumModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}
}