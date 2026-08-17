package br.com.caphard.client.screen.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;

final class RuleTooltipLine implements ClientTooltipComponent {
	private static final int VANILLA_LINE_HEIGHT = 10;
	private static final int ADDITIONAL_LINE_SPACING = 2;

	private final FormattedCharSequence text;
	private final boolean first;
	private final boolean last;

	RuleTooltipLine(FormattedCharSequence text, boolean first, boolean last) {
		this.text = text;
		this.first = first;
		this.last = last;
	}

	@Override
	public int getHeight(Font font) {
		return this.first || this.last
			? VANILLA_LINE_HEIGHT
			: VANILLA_LINE_HEIGHT + ADDITIONAL_LINE_SPACING;
	}

	@Override
	public int getWidth(Font font) {
		return font.width(this.text);
	}

	@Override
	public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
		graphics.text(font, this.text, x, y, -1, true);
	}
}
