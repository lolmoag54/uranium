package net.mcreator.uranium.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.uranium.world.inventory.ReprocessingplantguiMenu;
import net.mcreator.uranium.procedures.RcrepprogressProcedure;
import net.mcreator.uranium.procedures.RcreppowerProcedure;
import net.mcreator.uranium.init.UraniumModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class ReprocessingplantguiScreen extends AbstractContainerScreen<ReprocessingplantguiMenu> implements UraniumModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final Identifier BACKGROUND = Identifier.parse("uranium:textures/screens/reprocessingplantgui.png");
	private static final Identifier SPRITE_0 = Identifier.parse("uranium:textures/screens/rc_m_rep.png");
	private static final Identifier SPRITE_1 = Identifier.parse("uranium:textures/screens/rc_bar_energy.png");
	private static final Identifier SPRITE_2 = Identifier.parse("uranium:textures/screens/rc_arrow.png");

	public ReprocessingplantguiScreen(ReprocessingplantguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
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
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + -1, this.topPos + 0, 0, 0, 176, 176, 176, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + 9, this.topPos + 20, 0, Mth.clamp((int) RcreppowerProcedure.execute(world, x, y, z) * 50, 0, 1000), 50, 50, 50, 1050);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, this.leftPos + 81, this.topPos + 41, 0, Mth.clamp((int) RcrepprogressProcedure.execute(world, x, y, z) * 38, 0, 304), 38, 38, 38, 342);
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
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_title"), 7, 5, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_fe"), 11, 71, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_in0"), 53, 24, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_in1"), 53, 44, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_in2"), 53, 64, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_out0"), 145, 24, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_out1"), 145, 44, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.reprocessingplantgui.label_out2"), 145, 64, -3550246, false);
	}

	@Override
	public void init() {
		super.init();
	}
}