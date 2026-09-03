package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public abstract class RainSplashDensityMixin {
	@Unique private int caphard$rainSplashBudget;

	@Redirect(method = "tickWeatherEffects", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
	private void caphard$thinRainSplashes(ClientLevel level, ParticleOptions particle,
		double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		if (CapHardConfig.reducedRainEffects() && particle == ParticleTypes.RAIN) {
			// Keep three of every five actual splash requests, even at low rain intensity.
			// Leave vanilla random sampling and the rain sound selection untouched.
			this.caphard$rainSplashBudget += 3;
			if (this.caphard$rainSplashBudget < 5) {
				return;
			}
			this.caphard$rainSplashBudget -= 5;
		}
		level.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
	}
}
