package net.mcreator.uranium.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.uranium.world.inventory.ReactorcontrolguiMenu;
import net.mcreator.uranium.procedures.*;
import net.mcreator.uranium.network.ReactorcontrolguiButtonMessage;
import net.mcreator.uranium.init.UraniumModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class ReactorcontrolguiScreen extends AbstractContainerScreen<ReactorcontrolguiMenu> implements UraniumModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_vent_auto;
	private Button button_vent_open;
	private Button button_vent_closed;
	private Button button_scram;
	private Button button_dismantle;
	private Button button_depth_minus;
	private Button button_depth_plus;
	private static final Identifier BACKGROUND = Identifier.parse("uranium:textures/screens/reactorcontrolgui.png");
	private static final Identifier SPRITE_0 = Identifier.parse("uranium:textures/screens/rc_panel.png");
	private static final Identifier SPRITE_1 = Identifier.parse("uranium:textures/screens/rc_gauge_normal.png");
	private static final Identifier SPRITE_2 = Identifier.parse("uranium:textures/screens/rc_gauge_heat.png");
	private static final Identifier SPRITE_3 = Identifier.parse("uranium:textures/screens/rc_gauge_normal.png");
	private static final Identifier SPRITE_4 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_5 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_6 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_7 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_8 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_9 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_10 = Identifier.parse("uranium:textures/screens/rc_digits.png");
	private static final Identifier SPRITE_11 = Identifier.parse("uranium:textures/screens/rc_bar_fuel.png");
	private static final Identifier SPRITE_12 = Identifier.parse("uranium:textures/screens/rc_bar_tank.png");
	private static final Identifier SPRITE_13 = Identifier.parse("uranium:textures/screens/rc_bar_heat.png");
	private static final Identifier SPRITE_14 = Identifier.parse("uranium:textures/screens/rc_bar_water.png");
	private static final Identifier SPRITE_15 = Identifier.parse("uranium:textures/screens/rc_status.png");
	private static final Identifier SPRITE_16 = Identifier.parse("uranium:textures/screens/rc_ventmark.png");
	private static final Identifier SPRITE_17 = Identifier.parse("uranium:textures/screens/rc_depthbar.png");

	public ReactorcontrolguiScreen(ReactorcontrolguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 331, 240);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + 0, this.topPos + 0, 0, 0, 331, 331, 331, 331);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + 22, this.topPos + 18, 0, Mth.clamp((int) RcgaugesteamProcedure.execute(world, x, y, z) * 44, 0, 1760), 44, 44, 44, 1804);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, this.leftPos + 82, this.topPos + 18, 0, Mth.clamp((int) RcgaugeheatProcedure.execute(world, x, y, z) * 44, 0, 1760), 44, 44, 44, 1804);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_3, this.leftPos + 142, this.topPos + 18, 0, Mth.clamp((int) RcgaugefuelProcedure.execute(world, x, y, z) * 44, 0, 1760), 44, 44, 44, 1804);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_4, this.leftPos + 37, this.topPos + 44, 0, Mth.clamp((int) Rcsteamd1Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_5, this.leftPos + 44, this.topPos + 44, 0, Mth.clamp((int) Rcsteamd2Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_6, this.leftPos + 97, this.topPos + 44, 0, Mth.clamp((int) Rcheatd1Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_7, this.leftPos + 104, this.topPos + 44, 0, Mth.clamp((int) Rcheatd2Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_8, this.leftPos + 154, this.topPos + 44, 0, Mth.clamp((int) Rcfueld1Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_9, this.leftPos + 161, this.topPos + 44, 0, Mth.clamp((int) Rcfueld2Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_10, this.leftPos + 168, this.topPos + 44, 0, Mth.clamp((int) Rcfueld3Procedure.execute(world, x, y, z) * 8, 0, 80), 8, 8, 8, 88);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_11, this.leftPos + 12, this.topPos + 82, 0, Mth.clamp((int) RcbarfuelProcedure.execute(world, x, y, z) * 50, 0, 1000), 50, 50, 50, 1050);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_12, this.leftPos + 34, this.topPos + 82, 0, Mth.clamp((int) RcbartankProcedure.execute(world, x, y, z) * 50, 0, 1000), 50, 50, 50, 1050);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_13, this.leftPos + 56, this.topPos + 82, 0, Mth.clamp((int) RcbarheatProcedure.execute(world, x, y, z) * 50, 0, 1000), 50, 50, 50, 1050);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_14, this.leftPos + 78, this.topPos + 82, 0, Mth.clamp((int) RcbarwaterProcedure.execute(world, x, y, z) * 50, 0, 1000), 50, 50, 50, 1050);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_15, this.leftPos + 208, this.topPos + 18, 0, Mth.clamp((int) RcstatusProcedure.execute(world, x, y, z) * 118, 0, 236), 118, 118, 118, 354);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_16, this.leftPos + 104, this.topPos + 110, 0, Mth.clamp((int) RcventmarkProcedure.execute(world, x, y, z) * 98, 0, 196), 98, 98, 98, 294);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_17, this.leftPos + 228, this.topPos + 117, 0, Mth.clamp((int) RcdepthbarProcedure.execute(world, x, y, z) * 78, 0, 1170), 78, 78, 78, 1248);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_reactor_control"), 8, 5, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_steam"), 29, 64, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_heat"), 92, 64, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_fuel"), 152, 64, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_bar_fuel"), 11, 136, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_bar_tank"), 33, 136, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_bar_heat"), 55, 136, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_bar_water"), 78, 136, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_fuel_rod"), 228, 61, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_spent"), 228, 83, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_water_in"), 292, 61, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_bucket_out"), 292, 83, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_vent"), 106, 82, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reactorcontrolgui.label_depth"), 209, 105, -3550246, false);
	}

	@Override
	public void init() {
		super.init();
		button_vent_auto = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_vent_auto"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(0, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 104, this.topPos + 92, 28, 16).build();
		this.addRenderableWidget(button_vent_auto);
		button_vent_open = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_vent_open"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(1, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 134, this.topPos + 92, 28, 16).build();
		this.addRenderableWidget(button_vent_open);
		button_vent_closed = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_vent_closed"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(2, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}).bounds(this.leftPos + 164, this.topPos + 92, 36, 16).build();
		this.addRenderableWidget(button_vent_closed);
		button_scram = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_scram"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(3, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}).bounds(this.leftPos + 104, this.topPos + 117, 96, 16).build();
		this.addRenderableWidget(button_scram);
		button_dismantle = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_dismantle"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(4, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}).bounds(this.leftPos + 104, this.topPos + 137, 96, 16).build();
		this.addRenderableWidget(button_dismantle);
		button_depth_minus = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_depth_minus"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(5, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}).bounds(this.leftPos + 208, this.topPos + 116, 18, 16).build();
		this.addRenderableWidget(button_depth_minus);
		button_depth_plus = Button.builder(Component.translatable("gui.uranium.reactorcontrolgui.button_depth_plus"), e -> {
			int x = ReactorcontrolguiScreen.this.x;
			int y = ReactorcontrolguiScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ReactorcontrolguiButtonMessage(6, x, y, z));
				ReactorcontrolguiButtonMessage.handleButtonAction(entity, 6, x, y, z);
			}
		}).bounds(this.leftPos + 308, this.topPos + 116, 18, 16).build();
		this.addRenderableWidget(button_depth_plus);
	}
}