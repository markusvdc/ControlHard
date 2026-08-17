package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(LivingEntity.class)
public abstract class LivingEntityHardRulesMixin {
	private static final float SPEAR_DAMAGE_MULTIPLIER = 0.70F;

	@ModifyConstant(method = "hurtServer", constant = @Constant(floatValue = 10.0F))
	private float caphard$useFullHorseInvulnerabilityTime(float vanillaThreshold) {
		LivingEntity entity = (LivingEntity)(Object)this;
		return entity instanceof AbstractHorse && HardRules.isActive(entity.level(), HardRule.HORSE_IMMUNITY)
			? 0.0F
			: vanillaThreshold;
	}

	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float caphard$reduceSpearDamage(
		float damage,
		ServerLevel level,
		DamageSource source
	) {
		return HardRules.isActive(level, HardRule.SPEAR_DAMAGE) && source.is(DamageTypes.SPEAR)
			? damage * SPEAR_DAMAGE_MULTIPLIER
			: damage;
	}
}
