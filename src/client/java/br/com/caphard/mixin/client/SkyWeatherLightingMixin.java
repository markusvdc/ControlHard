package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkyRenderer.class)
public abstract class SkyWeatherLightingMixin {
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
	private float caphard$preserveCelestialBrightness(ClientLevel level, float partialTick) {
		return CapHardConfig.clearWeatherLighting() ? 0.0F : level.getRainLevel(partialTick);
	}
}
