package br.com.caphard.client.screen.component;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public final class OptionTooltip {
	private static final int LORE_COLOR = 0xFDDF93;

	private OptionTooltip() {
	}

	public static Tooltip create(String optionKey) {
		return create(optionKey, optionKey);
	}

	public static Tooltip create(String loreOptionKey, String descriptionOptionKey) {
		String specificDescriptionKey = loreOptionKey + ".description";
		String descriptionKey = Language.getInstance().has(specificDescriptionKey)
			? specificDescriptionKey
			: descriptionOptionKey + ".description";
		Component description = Component.translatable(descriptionKey);
		String loreKey = loreOptionKey + ".lore";
		if (!Language.getInstance().has(loreKey)) {
			return Tooltip.create(description);
		}

		Component lore = Component.translatable(loreKey).withStyle(style -> style.withColor(LORE_COLOR));
		return Tooltip.create(Component.translatable("caphard.option_tooltip", lore, description));
	}
}
