package br.com.caphard.client.screen.component;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public final class RuleTooltip {
	private static final int LORE_COLOR = 0xFDDF93;

	private final Component lore;
	private final Component description;

	private RuleTooltip(Component lore, Component description) {
		this.lore = lore;
		this.description = description;
	}

	public static RuleTooltip empty() {
		return new RuleTooltip(Component.empty(), Component.empty());
	}

	public static RuleTooltip translated(String optionKey) {
		String loreKey = optionKey + ".lore";
		Component lore = !I18n.get(loreKey).equals(loreKey)
			? Component.translatable(loreKey).withColor(LORE_COLOR)
			: Component.empty();
		return new RuleTooltip(lore, Component.translatable(optionKey + ".description"));
	}

	public List<FormattedCharSequence> split(Font font, int maxWidth) {
		List<FormattedCharSequence> lines = new ArrayList<>();
		if (!this.lore.getString().isEmpty()) {
			lines.addAll(font.split(this.lore, maxWidth));
			lines.add(FormattedCharSequence.EMPTY);
		}
		lines.addAll(font.split(this.description, maxWidth));
		return lines;
	}

	public Component narration() {
		return this.lore.getString().isEmpty()
			? this.description
			: Component.empty().append(this.lore).append(". ").append(this.description);
	}
}
