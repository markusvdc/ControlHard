package br.com.caphard.mixin;

import br.com.caphard.gameplay.CapHardAttributes;
import br.com.caphard.gameplay.ContainerRemainderPolicy;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataComponentHolder.class)
public interface DataComponentHolderMixin {
	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private <T> void caphard$overrideFoodComponents(
		DataComponentType<? extends T> type,
		CallbackInfoReturnable<T> callback
	) {
		if (!((Object) this instanceof ItemStack stack)) {
			return;
		}
		if (ContainerRemainderPolicy.suppresses(stack, type)) {
			callback.setReturnValue(null);
			return;
		}

		T override = CapHardAttributes.overrideComponent(stack, type);
		if (override != null) {
			callback.setReturnValue(override);
		}
	}

	@Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
	private <T> void caphard$overrideFoodComponentsOrDefault(
		DataComponentType<? extends T> type,
		T defaultValue,
		CallbackInfoReturnable<T> callback
	) {
		if (!((Object) this instanceof ItemStack stack)) {
			return;
		}
		if (ContainerRemainderPolicy.suppresses(stack, type)) {
			callback.setReturnValue(defaultValue);
			return;
		}

		T override = CapHardAttributes.overrideComponent(stack, type);
		if (override != null) {
			callback.setReturnValue(override);
		}
	}

	@Inject(method = "has", at = @At("HEAD"), cancellable = true)
	private void caphard$reportFoodComponents(
		DataComponentType<?> type,
		CallbackInfoReturnable<Boolean> callback
	) {
		if ((Object) this instanceof ItemStack stack) {
			if (ContainerRemainderPolicy.suppresses(stack, type)) {
				callback.setReturnValue(false);
			} else if (CapHardAttributes.hasOverride(stack, type)) {
				callback.setReturnValue(true);
			}
		}
	}

}
