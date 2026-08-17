package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SuspiciousStewEffects.class)
public abstract class SuspiciousStewEffectsTooltipMixin {
	@Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
	private void caphard$hideSpecificEffectWhileShowingPossibilities(
		Item.TooltipContext context,
		Consumer<Component> consumer,
		TooltipFlag flag,
		DataComponentGetter components,
		CallbackInfo callback
	) {
		if (CapHardConfig.showFoodProperties() && Minecraft.getInstance().hasShiftDown()) {
			callback.cancel();
		}
	}
}
