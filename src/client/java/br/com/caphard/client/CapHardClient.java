package br.com.caphard.client;

import br.com.caphard.config.CapHardConfig;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public final class CapHardClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			if (CapHardConfig.showEnchantmentInformation() && Minecraft.getInstance().hasShiftDown()) {
				EnchantmentInformationTooltip.appendBookInformation(stack, lines);
			}

			if (!CapHardConfig.showFoodProperties()
				|| (!CapHardConfig.isAllowed(stack.getItem()) && !stack.is(Items.COOKED_BEEF))
				|| !Minecraft.getInstance().hasShiftDown()) {
				return;
			}

			TooltipProperties properties = getProperties(stack);
			if (properties == null) {
				return;
			}

			lines.add(Component.translatable("caphard.tooltip.hunger", properties.hunger()).withStyle(ChatFormatting.GRAY));
			lines.add(Component.translatable(
				"caphard.tooltip.saturation",
				formatDecimal(properties.saturation())
			).withStyle(ChatFormatting.GRAY));
			lines.add(Component.translatable(
				"caphard.tooltip.speed",
				formatDecimal(properties.consumeSeconds())
			).withStyle(ChatFormatting.GRAY));

			if (stack.is(Items.SUSPICIOUS_STEW)) {
				addPossibleSuspiciousStewEffects(lines);
			}
		});
	}

	private static void addPossibleSuspiciousStewEffects(java.util.List<Component> lines) {
		Set<Holder<MobEffect>> possibleEffects = new LinkedHashSet<>();
		for (SuspiciousEffectHolder holder : SuspiciousEffectHolder.getAllEffectHolders()) {
			holder.getSuspiciousEffects().effects().forEach(entry -> possibleEffects.add(entry.effect()));
		}

		lines.add(Component.translatable("caphard.tooltip.possible_effects").withStyle(ChatFormatting.GRAY));
		for (Holder<MobEffect> effect : possibleEffects) {
			lines.add(Component.translatable(
				"caphard.tooltip.possible_effect",
				effect.value().getDisplayName()
			).withStyle(ChatFormatting.GRAY));
		}
	}

	private static TooltipProperties getProperties(ItemStack stack) {
		FoodProperties food = stack.get(DataComponents.FOOD);
		Consumable consumable = stack.get(DataComponents.CONSUMABLE);

		if (food != null && consumable != null) {
			return new TooltipProperties(food.nutrition(), food.saturation(), consumable.consumeSeconds());
		}

		if (stack.is(Items.CAKE)) {
			return new TooltipProperties(2, 0.4F, 0.0F);
		}

		return null;
	}

	private static String formatDecimal(float value) {
		return String.format(Locale.ROOT, "%.1f", value);
	}

	private record TooltipProperties(int hunger, float saturation, float consumeSeconds) {
	}
}
