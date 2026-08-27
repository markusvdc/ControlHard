package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "org.anti_ad.mc.ipnext.item.rule.natives.DefinedNativeRulesKt", remap = false)
public abstract class InventoryProfilesDisplayNameMixin {
	@Redirect(
		method = "display_name_delegate$lambda$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;getDisplayName()Lnet/minecraft/network/chat/Component;",
			remap = true
		),
		require = 0,
		remap = false
	)
	private static Component caphard$sortSpecialItemsByDescription(ItemStack stack) {
		Component displayName = stack.getDisplayName();
		if (!CapHardConfig.sortSpecialItemsByDescription()) {
			return displayName;
		}

		if (!stack.is(Items.ENCHANTED_BOOK) && !stack.is(Items.OMINOUS_BOTTLE)) {
			return displayName;
		}

		String sortKey = displayName.getString();
		if (stack.is(Items.ENCHANTED_BOOK)) {
			sortKey = enchantmentSortKey(sortKey, stack);
		} else {
			OminousBottleAmplifier amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
			if (amplifier != null) {
				sortKey += "\u0000" + numericKey(amplifier.value() + 1);
			}
		}
		return Component.literal(sortKey);
	}

	private static String enchantmentSortKey(String displayName, ItemStack stack) {
		ItemEnchantments enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
		if (enchantments == null || enchantments.isEmpty()) {
			return displayName;
		}

		List<EnchantmentKey> keys = new ArrayList<>();
		for (var entry : enchantments.entrySet()) {
			keys.add(new EnchantmentKey(entry.getKey().value().description().getString(), entry.getIntValue()));
		}
		Collator collator = Collator.getInstance(Locale.getDefault());
		collator.setStrength(Collator.PRIMARY);
		keys.sort((left, right) -> {
			int nameOrder = collator.compare(left.name(), right.name());
			return nameOrder != 0 ? nameOrder : Integer.compare(left.level(), right.level());
		});

		StringBuilder key = new StringBuilder(displayName);
		for (EnchantmentKey enchantment : keys) {
			key.append('\u0000').append(enchantment.name()).append('\u0000').append(numericKey(enchantment.level()));
		}
		return key.toString();
	}

	private static String numericKey(int value) {
		return String.format(Locale.ROOT, "%05d", value);
	}

	private record EnchantmentKey(String name, int level) {
	}

}
