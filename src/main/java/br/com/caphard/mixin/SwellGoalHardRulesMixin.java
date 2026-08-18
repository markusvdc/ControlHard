package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRule;
import br.com.caphard.gameplay.HardRules;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SwellGoal.class)
public abstract class SwellGoalHardRulesMixin {
	@Redirect(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Creeper;setSwellDir(I)V"
		)
	)
	private void caphard$keepExplosionCharging(Creeper creeper, int direction) {
		if (direction < 0
			&& creeper.getSwellDir() > 0
			&& HardRules.isActive(creeper.level(), HardRule.INEVITABLE_EXPLOSION)) {
			creeper.setSwellDir(1);
			return;
		}
		creeper.setSwellDir(direction);
	}
}
