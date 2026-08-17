package br.com.caphard.mixin.client;

import br.com.caphard.client.screen.component.OptionTooltipLine;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Tooltip.class)
public abstract class TooltipMixin {
	private static final int CAPHARD_OPTION_TOOLTIP_WIDTH = 425;

	@Shadow
	@Final
	private Component message;

	@Inject(method = "toCharSequence", at = @At("HEAD"), cancellable = true)
	private void caphard$formatOptionTooltip(
		Minecraft minecraft,
		CallbackInfoReturnable<List<FormattedCharSequence>> callback
	) {
		if (this.message.getContents() instanceof TranslatableContents contents
			&& isOptionDescription(contents.getKey())) {
			List<FormattedCharSequence> lines = minecraft.font.split(
				this.message,
				CAPHARD_OPTION_TOOLTIP_WIDTH
			);
			List<FormattedCharSequence> spacedLines = new ArrayList<>(lines.size());
			for (int index = 0; index < lines.size(); index++) {
				spacedLines.add(new OptionTooltipLine(lines.get(index), index == 0));
			}
			callback.setReturnValue(spacedLines);
		}
	}

	private static boolean isOptionDescription(String key) {
		return key.equals("caphard.option_tooltip")
			|| key.endsWith(".description")
			&& (key.startsWith("caphard.options.")
				|| key.startsWith("caphard.rule.")
				|| key.equals("caphard.food.description"));
	}
}
