package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseHardRulesMixin {
	@ModifyArgs(
		method = "hurtServer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/animal/Animal;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
		)
	)
	private void caphard$scaleDamage(Args args) {
		ServerLevel level = args.get(0);
		DamageSource source = args.get(1);
		if (HardRules.isActive(level, HardRule.MOUNT_DAMAGE) && source.scalesWithDifficulty()) {
			args.set(2, (float)args.get(2) * 1.5F);
		}
	}

	@Inject(method = "hurtServer", at = @At("RETURN"))
	private void caphard$extendDamageCooldown(
		ServerLevel level,
		DamageSource source,
		float damage,
		CallbackInfoReturnable<Boolean> callback
	) {
		if (callback.getReturnValue() && HardRules.isActive(level, HardRule.HORSE_IMMUNITY)) {
			((AbstractHorse)(Object)this).invulnerableTime = 50;
		}
	}
}
