package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WeatherEffectRenderer.class)
public abstract class RainDensityMixin {
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/multiplayer/ClientLevel;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
	private Biome.Precipitation caphard$thinRainColumns(ClientLevel level, BlockPos pos) {
		Biome.Precipitation precipitation = level.getPrecipitationAt(pos);
		if (CapHardConfig.reducedRainEffects() && precipitation == Biome.Precipitation.RAIN) {
			// A world-position mask stays stable across frames and camera movement.
			// Skip before light sampling, column allocation and vertex generation.
			int hash = pos.getX() * 0x1f1f1f1f ^ pos.getZ() * 0x5f356495;
			hash ^= hash >>> 16;
			hash *= 0x45d9f3b;
			hash ^= hash >>> 16;
			if (Integer.remainderUnsigned(hash, 5) < 2) {
				return Biome.Precipitation.NONE;
			}
		}
		return precipitation;
	}
}
