package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerHardRulesMixin {
	private static final int CAPPED_EXPERIENCE_LEVEL = 50;
	private static final int CAPPED_EXPERIENCE_COST = 292;

	@ModifyConstant(method = "hurtServer", constant = @Constant(floatValue = 3.0F))
	private float caphard$applyHardDamage(
		float hardMultiplier,
		ServerLevel level,
		DamageSource source,
		float damage
	) {
		Player player = (Player)(Object)this;
		return HardRules.isActive(player.level(), HardRule.PLAYER_DAMAGE)
			&& !(source.getEntity() instanceof Warden)
			&& !(source.getEntity() instanceof EnderDragon)
			? 4.0F
			: hardMultiplier;
	}

	@Inject(method = "getXpNeededForNextLevel", at = @At("RETURN"), cancellable = true)
	private void caphard$capExperienceCost(CallbackInfoReturnable<Integer> callbackInfo) {
		Player player = (Player)(Object)this;
		if (player.experienceLevel >= CAPPED_EXPERIENCE_LEVEL
			&& HardRules.isActive(player.level(), HardRule.CAPPED_EXPERIENCE_COST)) {
			callbackInfo.setReturnValue(CAPPED_EXPERIENCE_COST);
		}
	}
}
