package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WeatherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherAttributes.class)
public abstract class WeatherLightingMixin {
	@Inject(method = "lambda$addLayer$0", at = @At("HEAD"), cancellable = true)
	private static void caphard$preserveClearWeatherVisuals(
		WeatherAttributes.WeatherAccess weather,
		EnvironmentAttributeMap.Entry<?, ?> rain,
		EnvironmentAttribute<?> attribute,
		EnvironmentAttributeMap.Entry<?, ?> thunder,
		Object value,
		int ticks,
		CallbackInfoReturnable<Object> callback
	) {
		// Preserve the incoming biome/time value; gameplay attributes retain real weather.
		if (CapHardConfig.clearWeatherLighting() && (
			attribute == EnvironmentAttributes.SKY_COLOR
			|| attribute == EnvironmentAttributes.FOG_COLOR
			|| attribute == EnvironmentAttributes.CLOUD_COLOR
			|| attribute == EnvironmentAttributes.SKY_LIGHT_COLOR
			|| attribute == EnvironmentAttributes.SKY_LIGHT_FACTOR
			|| attribute == EnvironmentAttributes.STAR_BRIGHTNESS
			|| attribute == EnvironmentAttributes.SUNRISE_SUNSET_COLOR
		)) {
			callback.setReturnValue(value);
		}
	}
}
