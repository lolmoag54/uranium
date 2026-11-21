package net.mcreator.uranium.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

import net.mcreator.uranium.procedures.*;
import net.mcreator.uranium.UraniumMod;

@EventBusSubscriber
public record ReactorcontrolguiButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<ReactorcontrolguiButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(UraniumMod.MODID, "reactorcontrolgui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ReactorcontrolguiButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ReactorcontrolguiButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ReactorcontrolguiButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ReactorcontrolguiButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ReactorcontrolguiButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleButtonAction(context.player(), message.buttonID, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (buttonID == 0) {

			RcventautoProcedure.execute(world, x, y, z);
		}
		if (buttonID == 1) {

			RcventopenProcedure.execute(world, x, y, z);
		}
		if (buttonID == 2) {

			RcventclosedProcedure.execute(world, x, y, z);
		}
		if (buttonID == 3) {

			RcscramProcedure.execute(world, x, y, z);
		}
		if (buttonID == 4) {

			RcdismantleProcedure.execute(world, x, y, z);
		}
		if (buttonID == 5) {

			RcdepthminusProcedure.execute(world, x, y, z);
		}
		if (buttonID == 6) {

			RcdepthplusProcedure.execute(world, x, y, z);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		UraniumMod.addNetworkMessage(ReactorcontrolguiButtonMessage.TYPE, ReactorcontrolguiButtonMessage.STREAM_CODEC, ReactorcontrolguiButtonMessage::handleData);
	}
}