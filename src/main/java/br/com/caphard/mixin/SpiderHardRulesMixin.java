package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.world.entity.monster.spider.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Spider.class)
public abstract class SpiderHardRulesMixin {
	@ModifyConstant(method = "finalizeSpawn", constant = @Constant(floatValue = 0.1F))
	private float caphard$increaseEffectChance(float chance) {
		Spider spider = (Spider)(Object)this;
		return HardRules.isActive(spider.level(), HardRule.SPIDER_EFFECT) ? 0.30F : chance;
	}
}
