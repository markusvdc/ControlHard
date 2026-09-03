package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelWeatherLightingMixin {
	@Inject(method = "getSkyFlashTime", at = @At("HEAD"), cancellable = true)
	private void caphard$hideWeatherFlash(CallbackInfoReturnable<Integer> callback) {
		if (CapHardConfig.clearWeatherLighting()) {
			callback.setReturnValue(0);
		}
	}
}
