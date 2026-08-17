package br.com.caphard.client.screen.component;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class RuleEntry {
	private final Minecraft minecraft;
	private final HardRule rule;
	private final Identifier texture;
	private final Component name;
	private final String tooltipKey;
	private final RuleTooltip tooltip;
	private final boolean category;
	private boolean selected;

	public RuleEntry(Minecraft minecraft, HardRule rule, Item icon) {
		this.minecraft = minecraft;
		this.rule = rule;
		this.texture = BuiltInRegistries.ITEM.getKey(icon).withPrefix("textures/item/").withSuffix(".png");
		this.name = Component.translatable("caphard.rule." + rule.id());
		this.tooltipKey = "caphard.rule." + rule.id();
		this.tooltip = RuleTooltip.translated(this.tooltipKey);
		this.category = false;
		this.selected = CapHardConfig.isRuleEnabled(rule);
	}

	private RuleEntry(Minecraft minecraft, Component name) {
		this.minecraft = minecraft;
		this.rule = null;
		this.texture = BuiltInRegistries.ITEM.getKey(Items.AIR).withPrefix("textures/item/").withSuffix(".png");
		this.name = name;
		this.tooltipKey = null;
		this.tooltip = RuleTooltip.empty();
		this.category = true;
	}

	public static RuleEntry category(Minecraft minecraft, String translationKey) {
		return new RuleEntry(minecraft, Component.translatable(translationKey));
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
		if (hovered) graphics.outline(x, y + 1, width, height - 3, 0xFFFFFFFF);
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

	public void toggle() { if (!this.category) this.selected = !this.selected; }
	public void setSelected(boolean selected) { if (!this.category) this.selected = selected; }
	public boolean isSelected() { return this.selected; }
	public HardRule rule() { return this.rule; }
	public boolean isCategory() { return this.category; }
	public Component name() { return this.name; }
	public String tooltipKey() { return this.tooltipKey; }
	public RuleTooltip tooltip() { return this.tooltip; }
}
