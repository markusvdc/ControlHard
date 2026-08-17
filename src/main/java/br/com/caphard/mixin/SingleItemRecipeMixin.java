package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleItemRecipe.class)
public abstract class SingleItemRecipeMixin {
	@Shadow
	protected abstract ItemStackTemplate result();

	@Inject(
		method = "matches(Lnet/minecraft/world/item/crafting/SingleRecipeInput;Lnet/minecraft/world/level/Level;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void caphard$gateChorusFruitSmoking(
		SingleRecipeInput input,
		Level level,
		CallbackInfoReturnable<Boolean> callback
	) {
		if ((Object)this instanceof SmokingRecipe
			&& input.item().is(Items.CHORUS_FRUIT)
			&& result().create().is(Items.POPPED_CHORUS_FRUIT)
			&& !CapHardConfig.isSelected(Items.POPPED_CHORUS_FRUIT)) {
			callback.setReturnValue(false);
		}
	}
}
