package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerHardRulesMixin {
	@ModifyConstant(method = "hurtServer", constant = @Constant(floatValue = 3.0F))
	private float caphard$applyHardDamage(float hardMultiplier) {
		Player player = (Player)(Object)this;
		return HardRules.isActive(player.level(), HardRule.PLAYER_DAMAGE)
			? 4.0F
			: hardMultiplier;
	}
}
