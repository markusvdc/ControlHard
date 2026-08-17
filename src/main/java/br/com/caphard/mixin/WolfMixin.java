package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin {
	@Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
	private void caphard$preventRottenFleshFeeding(
		ItemStack itemStack,
		CallbackInfoReturnable<Boolean> callback
	) {
		if (CapHardConfig.preventRottenFleshWolfFeeding() && itemStack.is(Items.ROTTEN_FLESH)) {
			callback.setReturnValue(false);
		}
	}
}
