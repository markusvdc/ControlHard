package br.com.caphard.client.screen.component;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public final class GlobalOptionEntry extends AbstractButton {
	private final Consumer<Boolean> onValueChange;
	private final String optionKey;
	private boolean selected;
	private int viewportTop = Integer.MIN_VALUE;
	private int viewportBottom = Integer.MAX_VALUE;

	public void setViewport(int top, int bottom) {
		this.viewportTop = top;
		this.viewportBottom = bottom;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseY >= this.viewportTop && mouseY < this.viewportBottom && super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		return isMouseOver(event.x(), event.y()) && super.mouseClicked(event, doubleClick);
	}

	public GlobalOptionEntry(
		int x,
		int y,
		int width,
		int height,
		Component label,
		String optionKey,
		boolean selected,
		Consumer<Boolean> onValueChange
	) {
		super(x, y, width, height, label);
		this.selected = selected;
		this.onValueChange = onValueChange;
		this.optionKey = optionKey;
	}

	public void renderTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.isHoveredOrFocused()) {
			OptionTooltipRenderer.render(Minecraft.getInstance(), graphics, this.optionKey, mouseX, mouseY);
		}
	}

	@Override
	public void onPress(InputWithModifiers input) {
		setSelected(!this.selected);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		this.onValueChange.accept(selected);
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int x = this.getX();
		int y = this.getY();
		boolean highlighted = this.isHoveredOrFocused();

		graphics.fill(x, y + 1, x + this.width, y + this.height - 2, highlighted ? 0xCC333333 : 0x99202020);
		graphics.fill(x, y + 1, x + 1, y + this.height - 2, this.selected ? 0xFF79C64A : 0xFF555555);
		if (highlighted) {
			graphics.outline(x, y + 1, this.width, this.height - 3, 0xFFFFFFFF);
		}

		int contentY = y + (this.height - 11) / 2;
		drawCheckbox(graphics, x + 8, contentY);
		graphics.text(Minecraft.getInstance().font, this.getMessage(), x + 29, y + (this.height - 9) / 2, 0xFFFFFFFF, true);
	}

	private void drawCheckbox(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.fill(x, y, x + 11, y + 11, 0xFF111111);
		graphics.fill(x + 1, y + 1, x + 10, y + 10, 0xFF8B8B8B);
		graphics.fill(x + 2, y + 2, x + 9, y + 9, 0xFF252525);
		if (this.selected) {
			graphics.fill(x + 3, y + 5, x + 5, y + 8, 0xFF8EE36B);
			graphics.fill(x + 5, y + 7, x + 7, y + 9, 0xFF8EE36B);
			graphics.fill(x + 7, y + 3, x + 9, y + 8, 0xFF8EE36B);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		this.defaultButtonNarrationText(output);
	}
}
