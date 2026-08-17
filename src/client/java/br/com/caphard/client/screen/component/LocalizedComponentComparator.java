package br.com.caphard.client.screen.component;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class LocalizedComponentComparator {
	private LocalizedComponentComparator() {
	}

	public static Comparator<Component> forCurrentLanguage(Minecraft minecraft) {
		String languageCode = minecraft.getLanguageManager().getSelected();
		Locale locale = Locale.forLanguageTag(languageCode.replace('_', '-'));
		Collator collator = Collator.getInstance(locale);
		collator.setStrength(Collator.PRIMARY);
		return (first, second) -> collator.compare(first.getString(), second.getString());
	}
}
