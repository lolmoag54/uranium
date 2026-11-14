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

import net.mcreator.uranium.world.inventory.CentrifugeguiMenu;
import net.mcreator.uranium.procedures.ProgressbarcentrifugeguiProcedure;
import net.mcreator.uranium.procedures.CentrifugeenergyprocedureProcedure;
import net.mcreator.uranium.init.UraniumModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class CentrifugeguiScreen extends AbstractContainerScreen<CentrifugeguiMenu> implements UraniumModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final Identifier BACKGROUND = Identifier.parse("uranium:textures/screens/centrifugegui.png");
	private static final Identifier SPRITE_0 = Identifier.parse("uranium:textures/screens/public.png");
	private static final Identifier SPRITE_1 = Identifier.parse("uranium:textures/screens/public.png");

	public CentrifugeguiScreen(CentrifugeguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 174, 149);
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
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + 68, this.topPos + 25, 0, Mth.clamp((int) ProgressbarcentrifugeguiProcedure.execute(world, x, y, z) * 16, 0, 128), 32, 16, 32, 144);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + 5, this.topPos + 43, 0, Mth.clamp((int) CentrifugeenergyprocedureProcedure.execute(world, x, y, z) * 16, 0, 128), 32, 16, 32, 144);
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
		guiGraphics.text(this.font, Component.translatable("gui.uranium.centrifugegui.label_centrifuge"), 5, 3, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
	}
}