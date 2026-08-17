package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ContainerRemainderPolicy {
	private ContainerRemainderPolicy() {
	}

	public static boolean suppresses(ItemStack stack, DataComponentType<?> type) {
		if (type != DataComponents.USE_REMAINDER || !CapHardConfig.consumeContainer()) {
			return false;
		}

		if (!stack.getComponents().has(DataComponents.USE_REMAINDER)) {
			return false;
		}

		boolean isAllowedFood = CapHardConfig.isAllowed(stack.getItem())
			&& stack.getComponents().has(DataComponents.FOOD);
		boolean isDrinkablePotion = stack.is(Items.POTION);
		return isAllowedFood || isDrinkablePotion;
	}
}
