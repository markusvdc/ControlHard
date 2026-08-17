package br.com.caphard.client.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public final class CategoryDivider extends AbstractWidget {
	public CategoryDivider(int x, int y, int width, int height, Component label) {
		super(x, y, width, height, label);
		this.active = false;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int lineY = this.getY() + this.height / 2;
		int labelY = this.getY() + (this.height - 9) / 2;
		int labelWidth = Minecraft.getInstance().font.width(this.getMessage());
		graphics.fill(this.getX(), lineY, this.getX() + this.width, lineY + 1, 0xFF4A4A4A);
		graphics.fill(this.getX() + 7, labelY - 6, this.getX() + 15 + labelWidth, labelY + 11, 0xFF101010);
		graphics.text(Minecraft.getInstance().font, this.getMessage(), this.getX() + 11, labelY, 0xFFFFD36A, true);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
	}
}
