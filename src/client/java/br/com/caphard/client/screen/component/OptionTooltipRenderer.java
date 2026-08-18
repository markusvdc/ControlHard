package br.com.caphard.client.screen.component;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public final class OptionTooltipRenderer {
	private static final int MAX_WIDTH = 425;
	private static final int LINE_HEIGHT = 12;
	private static final int LORE_COLOR = 0xFDDF93;
	private static final ClientTooltipPositioner POSITIONER = new CursorFollowingPositioner();

	private OptionTooltipRenderer() {
	}

	public static void render(
		Minecraft minecraft,
		GuiGraphicsExtractor graphics,
		String optionKey,
		int mouseX,
		int mouseY
	) {
		List<FormattedCharSequence> lines = new ArrayList<>();
		Component lore = Component.translatable(optionKey + ".lore").withColor(LORE_COLOR);
		Component description = Component.translatable(optionKey + ".description");
		lines.addAll(minecraft.font.split(lore, MAX_WIDTH));
		lines.add(FormattedCharSequence.EMPTY);
		lines.addAll(minecraft.font.split(description, MAX_WIDTH));
		graphics.tooltip(
			minecraft.font,
			List.of(new Content(lines)),
			mouseX,
			mouseY,
			POSITIONER,
			null
		);
	}

	private static final class CursorFollowingPositioner implements ClientTooltipPositioner {
		private static final int CURSOR_GAP = 12;
		private static final int SCREEN_MARGIN = 4;

		@Override
		public Vector2ic positionTooltip(
			int screenWidth,
			int screenHeight,
			int mouseX,
			int mouseY,
			int tooltipWidth,
			int tooltipHeight
		) {
			int x = mouseX + CURSOR_GAP;
			if (x + tooltipWidth > screenWidth - SCREEN_MARGIN) {
				x = mouseX - CURSOR_GAP - tooltipWidth;
			}
			int y = mouseY - CURSOR_GAP;
			if (y + tooltipHeight > screenHeight - SCREEN_MARGIN) {
				y = mouseY - CURSOR_GAP - tooltipHeight;
			}
			if (y < SCREEN_MARGIN) {
				y = mouseY + CURSOR_GAP;
			}
			return new Vector2i(
				Math.clamp(x, SCREEN_MARGIN, Math.max(SCREEN_MARGIN, screenWidth - tooltipWidth - SCREEN_MARGIN)),
				Math.clamp(y, SCREEN_MARGIN, Math.max(SCREEN_MARGIN, screenHeight - tooltipHeight - SCREEN_MARGIN))
			);
		}
	}

	private record Content(List<FormattedCharSequence> lines) implements ClientTooltipComponent {
		@Override
		public int getHeight(Font font) {
			return this.lines.size() * LINE_HEIGHT;
		}

		@Override
		public int getWidth(Font font) {
			return this.lines.stream().mapToInt(font::width).max().orElse(0);
		}

		@Override
		public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
			for (int index = 0; index < this.lines.size(); index++) {
				graphics.text(font, this.lines.get(index), x, y + index * LINE_HEIGHT, -1, true);
			}
		}
	}
}
