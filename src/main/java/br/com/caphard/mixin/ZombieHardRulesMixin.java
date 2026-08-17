package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Zombie.class)
public abstract class ZombieHardRulesMixin {
	@ModifyConstant(
		method = "populateDefaultEquipmentSlots",
		constant = @Constant(floatValue = 0.05F)
	)
	private float caphard$increaseIronWeaponChance(float chance) {
		Zombie zombie = (Zombie)(Object)this;
		return HardRules.isActive(zombie.level(), HardRule.ZOMBIE_IRON_EQUIPMENT) ? 0.30F : chance;
	}
}
