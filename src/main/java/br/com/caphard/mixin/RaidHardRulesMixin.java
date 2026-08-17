package br.com.caphard.mixin;

import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Raid.class, priority = 900)
public abstract class RaidHardRulesMixin {
	@Shadow
	private int numGroups;

	@ModifyVariable(method = "getDefaultNumSpawns", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private int caphard$reuseLastVanillaWave(int wave) {
		return Math.min(wave, 7);
	}

	@Redirect(
		method = "getDefaultNumSpawns",
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/raid/Raid;numGroups:I"),
		require = 0
	)
	private int caphard$reuseLastVanillaBonusWave(Raid raid) {
		return Math.min(this.numGroups, 7);
	}
}
