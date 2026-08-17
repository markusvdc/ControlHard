package br.com.caphard.mixin.client;

import br.com.caphard.client.HiddenInformationDetector;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "hasShiftDown", at = @At("HEAD"), cancellable = true)
	private void caphard$useForcedShiftState(CallbackInfoReturnable<Boolean> callback) {
		Boolean forcedShift = HiddenInformationDetector.forcedShift();
		if (forcedShift != null) {
			callback.setReturnValue(forcedShift);
		}
	}
}
