package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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

	@Inject(method = "getProjectile", at = @At("RETURN"), cancellable = true)
	private void caphard$provideArrowForInfinityBow(
		ItemStack weapon,
		CallbackInfoReturnable<ItemStack> callbackInfo
	) {
		if (!CapHardConfig.infinityWithoutArrow()
			|| !callbackInfo.getReturnValue().isEmpty()
			|| !(weapon.getItem() instanceof BowItem)) {
			return;
		}
		Player player = (Player)(Object)this;
		var infinity = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
			.getOrThrow(Enchantments.INFINITY);
		if (EnchantmentHelper.getItemEnchantmentLevel(infinity, weapon) > 0) {
			callbackInfo.setReturnValue(new ItemStack(Items.ARROW));
		}
	}

	@ModifyConstant(method = "hurtServer", constant = @Constant(floatValue = 3.0F))
	private float caphard$applyHardDamage(
		float hardMultiplier,
		ServerLevel level,
		DamageSource source,
		float damage
	) {
		Player player = (Player)(Object)this;
		if (source.getEntity() instanceof Creeper) {
			return HardRules.isActive(player.level(), HardRule.CREEPER_DAMAGE)
				? hardMultiplier * 1.5F
				: hardMultiplier;
		}
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
