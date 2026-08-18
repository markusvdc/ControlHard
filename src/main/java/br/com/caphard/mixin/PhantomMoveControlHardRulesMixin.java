package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRule;
import br.com.caphard.gameplay.HardRules;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomMoveControl")
public abstract class PhantomMoveControlHardRulesMixin {
	@Shadow
	private Phantom this$0;

	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.1F))
	private float caphard$increaseCollisionRecoverySpeed(float originalSpeed) {
		return increaseByTwentyPercent(originalSpeed);
	}

	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 1.8F))
	private float caphard$increaseForwardSpeed(float originalSpeed) {
		return increaseByTwentyPercent(originalSpeed);
	}

	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.2F, ordinal = 0))
	private float caphard$increaseTurningSpeed(float originalSpeed) {
		return increaseByTwentyPercent(originalSpeed);
	}

	private float increaseByTwentyPercent(float speed) {
		return HardRules.isActive(this.this$0.level(), HardRule.PHANTOM_SPEED) ? speed * 1.2F : speed;
	}
}
