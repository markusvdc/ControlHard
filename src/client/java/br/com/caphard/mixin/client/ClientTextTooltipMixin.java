package br.com.caphard.mixin.client;

import br.com.caphard.client.screen.component.OptionTooltipLine;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTextTooltip.class)
public abstract class ClientTextTooltipMixin {
	@Shadow
	@Final
	private FormattedCharSequence text;

	@Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
	private void caphard$spaceOptionTooltipLines(
		Font font,
		CallbackInfoReturnable<Integer> callback
	) {
		if (this.text instanceof OptionTooltipLine line && !line.first()) {
			callback.setReturnValue(12);
		}
	}
}
