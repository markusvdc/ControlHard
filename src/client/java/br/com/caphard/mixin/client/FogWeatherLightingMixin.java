package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class FogWeatherLightingMixin {
	@Shadow private float rainFogMultiplier;

	@Inject(method = "applyWeatherDarken", at = @At("HEAD"), cancellable = true)
	private static void caphard$preserveFogColor(int color, float rain, float thunder,
		CallbackInfoReturnable<Integer> callback) {
		if (CapHardConfig.clearWeatherLighting()) {
			callback.setReturnValue(color);
		}
	}

	@Inject(method = "updateRainFogState", at = @At("HEAD"), cancellable = true)
	private void caphard$preserveFogDistance(Camera camera, ClientLevel level, DeltaTracker deltaTracker,
		CallbackInfo callback) {
		if (CapHardConfig.clearWeatherLighting()) {
			this.rainFogMultiplier = 0.0F;
			callback.cancel();
		}
	}
}
