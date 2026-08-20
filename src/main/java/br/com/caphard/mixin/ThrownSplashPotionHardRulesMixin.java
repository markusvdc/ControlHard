package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRule;
import br.com.caphard.gameplay.HardRules;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownSplashPotion.class)
public abstract class ThrownSplashPotionHardRulesMixin {
	private static final float WITCH_SLOWNESS_DURATION_MULTIPLIER = 0.70F;

	@Redirect(
		method = "onHitAsPotion",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/effect/MobEffectInstance;mapDuration(Lit/unimi/dsi/fastutil/ints/Int2IntFunction;)I"
		)
	)
	private int caphard$shortenWitchSlowness(
		MobEffectInstance effect,
		Int2IntFunction durationMapper
	) {
		int duration = effect.mapDuration(durationMapper);
		ThrownSplashPotion potion = (ThrownSplashPotion)(Object)this;
		return potion.getOwner() instanceof Witch
			&& effect.getEffect().equals(MobEffects.SLOWNESS)
			&& HardRules.isActive(potion.level(), HardRule.WITCH_SLOWNESS_DURATION)
			? Math.round(duration * WITCH_SLOWNESS_DURATION_MULTIPLIER)
			: duration;
	}
}
