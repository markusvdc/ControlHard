package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
	@ModifyConstant(method = "getFieldOfViewModifier", constant = @Constant(floatValue = 0.1F))
	private float caphard$reduceSpyglassZoom(float vanillaModifier) {
		return CapHardConfig.spyglassTreasureVision() ? 1.0F : vanillaModifier;
	}
}
