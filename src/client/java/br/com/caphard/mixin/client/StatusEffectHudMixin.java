package br.com.caphard.mixin.client;

import br.com.caphard.client.StatusEffectHudRenderer;
import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class StatusEffectHudMixin {
	@Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
	private void caphard$extractStatusEffectPanel(
		GuiGraphicsExtractor graphics,
		DeltaTracker deltaTracker,
		CallbackInfo callback
	) {
		if (CapHardConfig.showStatusEffectPanel()) {
			StatusEffectHudRenderer.extract(graphics);
			callback.cancel();
		}
	}
}
