package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raids.class)
public abstract class RaidsHardRulesMixin {
	@Inject(method = "getOrCreateRaid", at = @At("RETURN"))
	private void caphard$useTwelveWaves(
		ServerLevel level,
		BlockPos pos,
		CallbackInfoReturnable<Raid> callback
	) {
		if (HardRules.isActive(level, HardRule.TWELVE_WAVE_RAIDS)) {
			((RaidAccessor)(Object)callback.getReturnValue()).caphard$setNumGroups(12);
		}
	}
}
