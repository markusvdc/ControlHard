package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackCustomNameMixin {
	@Inject(method = "getStyledHoverName", at = @At("RETURN"), cancellable = true)
	private void caphard$removeCustomNameItalics(CallbackInfoReturnable<Component> callback) {
		ItemStack stack = (ItemStack)(Object)this;
		if (!CapHardConfig.uprightCustomNames() || !stack.has(DataComponents.CUSTOM_NAME)) {
			return;
		}

		callback.setReturnValue(callback.getReturnValue().copy().withStyle(style -> style.withItalic(false)));
	}
}
