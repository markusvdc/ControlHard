package br.com.caphard.client.screen.component;

import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

public record OptionTooltipLine(FormattedCharSequence contents, boolean first) implements FormattedCharSequence {
	@Override
	public boolean accept(FormattedCharSink output) {
		return this.contents.accept(output);
	}
}
