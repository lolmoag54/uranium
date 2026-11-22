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

import net.mcreator.uranium.world.inventory.CalcinationfurnaceguiMenu;
import net.mcreator.uranium.procedures.RccalcprogressProcedure;
import net.mcreator.uranium.procedures.RccalcpowerProcedure;
import net.mcreator.uranium.init.UraniumModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class CalcinationfurnaceguiScreen extends AbstractContainerScreen<CalcinationfurnaceguiMenu> implements UraniumModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final Identifier BACKGROUND = Identifier.parse("uranium:textures/screens/calcinationfurnacegui.png");
	private static final Identifier SPRITE_0 = Identifier.parse("uranium:textures/screens/rc_m_calc.png");
	private static final Identifier SPRITE_1 = Identifier.parse("uranium:textures/screens/rc_bar_energy.png");
	private static final Identifier SPRITE_2 = Identifier.parse("uranium:textures/screens/rc_arrow.png");

	public CalcinationfurnaceguiScreen(CalcinationfurnaceguiMenu container, Inventory inventory, Component text) {
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
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + -1, this.topPos + 0, 0, 0, 1, 1, -1, -1);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + 9, this.topPos + 20, 0, Mth.clamp((int) RccalcpowerProcedure.execute(world, x, y, z) * 1, 0, -2), 1, 1, -1, -1);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, this.leftPos + 91, this.topPos + 39, 0, Mth.clamp((int) RccalcprogressProcedure.execute(world, x, y, z) * 1, 0, -2), 1, 1, -1, -1);
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
		guiGraphics.text(this.font, Component.translatable("gui.uranium.calcinationfurnacegui.label_title"), 7, 5, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.calcinationfurnacegui.label_fe"), 11, 71, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.calcinationfurnacegui.label_in0"), 53, 30, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.calcinationfurnacegui.label_in1"), 53, 56, -3550246, false);
		guiGraphics.text(this.font, Component.translatable("gui.uranium.calcinationfurnacegui.label_out0"), 141, 58, -3550246, false);
	}

	@Override
	public void init() {
		super.init();
	}
}