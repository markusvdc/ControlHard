package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectsInInventory.class)
public abstract class InventoryEffectsMixin {
	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
	private void caphard$hideInventoryEffects(
		GuiGraphicsExtractor graphics,
		int mouseX,
		int mouseY,
		CallbackInfo callback
	) {
		if (CapHardConfig.showStatusEffectPanel()) {
			callback.cancel();
		}
	}
}
