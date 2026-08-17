package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.world.entity.animal.bee.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bee.class)
public abstract class BeeMixin {
	@Shadow
	private void setHasStung(boolean hasStung) {
	}

	@Redirect(
		method = "doHurtTarget",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/bee/Bee;setHasStung(Z)V")
	)
	private void caphard$keepBeeAliveAfterSting(Bee bee, boolean hasStung) {
		if (!CapHardConfig.beesSurviveStinging()) {
			this.setHasStung(hasStung);
		}
	}
}
