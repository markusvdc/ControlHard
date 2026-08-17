package br.com.caphard.client.screen.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class CapBasePanel {
	private static final int HEIGHT = 65;
	private static final Identifier HARD_TEXTURE = Identifier.withDefaultNamespace("textures/item/iron_sword.png");

	public void render(GuiGraphicsExtractor graphics, Font font, int x, int y, int width) {
		graphics.fill(x, y, x + width, y + HEIGHT, 0xCC1E1E1E);
		graphics.fill(x, y, x + width, y + 1, 0xFF777777);
		graphics.fill(x, y + HEIGHT - 1, x + width, y + HEIGHT, 0xFF111111);
		graphics.fill(x, y, x + 1, y + HEIGHT, 0xFF777777);
		graphics.fill(x + width - 1, y, x + width, y + HEIGHT, 0xFF111111);
		graphics.fill(x + 1, y + 1, x + width - 1, y + 3, 0xFFB68C36);

		graphics.text(font, Component.translatable("caphard.base.label"), x + 10, y + 9, 0xFFFFD36A, true);
		graphics.blit(RenderPipelines.GUI_TEXTURED, HARD_TEXTURE, x + 10, y + 32, 0, 0, 16, 16, 16, 16);
		graphics.text(font, Component.translatable("caphard.base.name"), x + 32, y + 36, 0xFFFFFFFF, true);

		int statsStart = Math.max(x + 150, x + width - 285);
		drawStat(graphics, font, statsStart, y + 18, Component.translatable("caphard.base.rules"), "12");
		drawStat(graphics, font, statsStart + 90, y + 18, Component.translatable("caphard.base.damage"), "200%");
		drawStat(graphics, font, statsStart + 190, y + 18, Component.translatable("caphard.base.waves"), "12");
	}

	private void drawStat(GuiGraphicsExtractor graphics, Font font, int x, int y, Component label, String value) {
		graphics.text(font, label, x, y, 0xFF9A9A9A, false);
		graphics.text(font, value, x, y + 17, 0xFFFFFFFF, true);
	}
}
