package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalHardRulesMixin {
	@Shadow
	@Final
	protected PathfinderMob mob;

	@Shadow
	protected abstract boolean isTimeToAttack();

	@Shadow
	protected abstract void resetAttackCooldown();

	@Inject(method = "checkAndPerformAttack", at = @At("HEAD"), cancellable = true)
	private void caphard$redirectSelectedAttackersToHorse(LivingEntity target, CallbackInfo callback) {
		if (
			HardRules.isActive(this.mob.level(), HardRule.MOUNT_TARGETING)
				&& target instanceof Player player
				&& player.getVehicle() instanceof AbstractHorse horse
				&& this.mob.level() instanceof ServerLevel serverLevel
				&& horse.isAlive()
				&& (
					this.mob.getType() == EntityTypes.SPIDER
						|| this.mob.getType() == EntityTypes.CAVE_SPIDER
						|| this.mob.isBaby() && (
							this.mob.getType() == EntityTypes.ZOMBIE
								|| this.mob.getType() == EntityTypes.ZOMBIE_VILLAGER
						)
				)
				&& this.mob.isWithinMeleeAttackRange(horse)
				&& this.mob.getSensing().hasLineOfSight(horse)
				&& this.isTimeToAttack()
		) {
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			this.mob.doHurtTarget(serverLevel, horse);
			callback.cancel();
		}
	}
}
