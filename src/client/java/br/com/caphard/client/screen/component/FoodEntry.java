package br.com.caphard.client.screen.component;

import br.com.caphard.config.CapHardConfig;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class FoodEntry {
	private final Minecraft minecraft;
	private final Identifier itemId;
	private final Identifier texture;
	private final Component name;
	private final String tooltipKey;
	private final boolean category;
	private boolean selected;

	public FoodEntry(Minecraft minecraft, Item item) {
		this(
			minecraft,
			item,
			uppercaseNativeName(minecraft, item),
			"caphard.food." + BuiltInRegistries.ITEM.getKey(item).getPath(),
			false
		);
	}

	private FoodEntry(Minecraft minecraft, Item item, Component name, String tooltipKey, boolean category) {
		this.minecraft = minecraft;
		this.itemId = BuiltInRegistries.ITEM.getKey(item);
		this.texture = this.itemId.withPrefix("textures/item/").withSuffix(".png");
		this.name = name;
		this.tooltipKey = tooltipKey;
		this.category = category;
		this.selected = !category && CapHardConfig.isSelected(item);
	}

	public static FoodEntry category(Minecraft minecraft, String translationKey) {
		return new FoodEntry(
			minecraft,
			Items.AIR,
			Component.translatable(translationKey),
			null,
			true
		);
	}

	private static Component uppercaseNativeName(Minecraft minecraft, Item item) {
		String languageCode = minecraft.getLanguageManager().getSelected();
		Locale locale = Locale.forLanguageTag(languageCode.replace('_', '-'));
		String localizedName = Component.translatable(item.getDescriptionId()).getString();
		return Component.literal(localizedName.toUpperCase(locale));
	}

	public void renderBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height, boolean hovered) {
		if (this.category) {
			int lineY = y + height / 2;
			graphics.fill(x, lineY, x + width, lineY + 1, 0xFF4A4A4A);
			graphics.fill(x + 7, y + 3, x + 15 + this.minecraft.font.width(this.name), y + height - 5, 0xFF101010);
			return;
		}

		graphics.fill(x, y + 1, x + width, y + height - 2, hovered ? 0xCC333333 : 0x99202020);
		graphics.fill(x, y + 1, x + 1, y + height - 2, this.selected ? 0xFF79C64A : 0xFF555555);
		if (hovered) {
			graphics.outline(x, y + 1, width, height - 3, 0xFFFFFFFF);
		}
	}

	public void renderContent(GuiGraphicsExtractor graphics, int x, int y, int height) {
		if (this.category) {
			graphics.text(this.minecraft.font, this.name, x + 11, y + 9, 0xFFFFD36A, true);
			return;
		}

		drawCheckbox(graphics, x + 8, y + 9);
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, x + 29, y + 7, 0, 0, 16, 16, 16, 16);
		graphics.text(this.minecraft.font, this.name, x + 52, y + 11, 0xFFFFFFFF, true);
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

	public void toggle() {
		if (!this.category) {
			this.selected = !this.selected;
		}
	}

	public void setSelected(boolean selected) {
		if (!this.category) {
			this.selected = selected;
		}
	}

	public boolean isSelected() {
		return this.selected;
	}

	public Identifier itemId() {
		return this.itemId;
	}

	public boolean isCategory() {
		return this.category;
	}

	public Component name() {
		return this.name;
	}

	public String tooltipKey() {
		return this.tooltipKey;
	}
}
