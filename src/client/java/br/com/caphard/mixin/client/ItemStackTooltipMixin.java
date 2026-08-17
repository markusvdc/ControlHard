package br.com.caphard.mixin.client;

import br.com.caphard.client.HiddenInformationDetector;
import br.com.caphard.client.PotionRecipeTooltip;
import br.com.caphard.config.CapHardConfig;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {
	@Inject(method = "getTooltipLines", at = @At("RETURN"))
	private void caphard$markHiddenInformation(
		Item.TooltipContext context,
		Player player,
		TooltipFlag tooltipFlag,
		CallbackInfoReturnable<List<Component>> callback
	) {
		List<Component> currentLines = callback.getReturnValue();
		ItemStack stack = (ItemStack)(Object)this;
		if (CapHardConfig.showPotionRecipes() && Minecraft.getInstance().hasShiftDown()) {
			PotionRecipeTooltip.append(stack, currentLines);
		}

		if (!CapHardConfig.markHiddenInformation()
			|| HiddenInformationDetector.isDetecting()
			|| currentLines.isEmpty()) {
			return;
		}

		HiddenInformationDetector.begin();
		try {
			HiddenInformationDetector.forceShift(false);
			List<Component> regularLines = stack.getTooltipLines(context, player, tooltipFlag);
			HiddenInformationDetector.forceShift(true);
			List<Component> shiftedLines = stack.getTooltipLines(context, player, tooltipFlag);

			if (!details(regularLines).equals(details(shiftedLines))) {
				currentLines.set(0, currentLines.getFirst().copy().append(" *"));
			}
		} finally {
			HiddenInformationDetector.end();
		}
	}

	private static List<Component> details(List<Component> lines) {
		return lines.isEmpty() ? List.of() : lines.subList(1, lines.size());
	}
}
