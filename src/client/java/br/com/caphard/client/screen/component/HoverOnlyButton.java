package br.com.caphard.client.screen.component;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public final class HoverOnlyButton extends Button {
	private final boolean silent;

	public HoverOnlyButton(int x, int y, int width, int height, Component message, OnPress onPress) {
		this(x, y, width, height, message, onPress, false);
	}

	public HoverOnlyButton(int x, int y, int width, int height, Component message, OnPress onPress, boolean silent) {
		super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
		this.silent = silent;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		this.extractDefaultSprite(graphics);
		this.extractDefaultLabel(
			graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE)
		);
	}

	@Override
	public boolean isHoveredOrFocused() {
		return this.isHovered();
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (!this.silent) {
			super.playDownSound(soundManager);
		}
	}
}
