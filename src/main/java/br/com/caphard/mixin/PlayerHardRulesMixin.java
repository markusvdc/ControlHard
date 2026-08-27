package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerHardRulesMixin {
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
}
