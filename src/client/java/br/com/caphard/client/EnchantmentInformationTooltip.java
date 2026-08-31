package br.com.caphard.client;

import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class EnchantmentInformationTooltip {
	private static final int TOOLTIP_PHYSICAL_MAX_WIDTH = 600;
	private static final int TOOLTIP_HORIZONTAL_PADDING = 8;
	private static final Set<String> SUPPORTED_ENCHANTMENTS = Set.of(
		"aqua_affinity", "bane_of_arthropods", "binding_curse", "blast_protection", "breach",
		"channeling", "density", "depth_strider", "efficiency", "feather_falling", "fire_aspect",
		"fire_protection", "flame", "fortune", "frost_walker", "impaling", "infinity", "knockback",
		"looting", "loyalty", "luck_of_the_sea", "lunge", "lure", "mending", "multishot", "piercing",
		"power", "projectile_protection", "protection", "punch", "quick_charge", "respiration", "riptide",
		"sharpness", "silk_touch", "smite", "soul_speed", "sweeping_edge", "swift_sneak", "thorns",
		"unbreaking", "vanishing_curse", "wind_burst"
	);

	private EnchantmentInformationTooltip() {
	}

	public static void appendBookInformation(ItemStack stack, List<Component> lines) {
		if (!stack.is(Items.ENCHANTED_BOOK)) {
			return;
		}
		ItemEnchantments enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
		if (enchantments == null || enchantments.size() != 1) {
			return;
		}
		for (Holder<Enchantment> enchantment : enchantments.keySet()) {
			if (hasInformation(enchantment)) {
				lines.add(CommonComponents.EMPTY);
				append(enchantment, lines);
			}
		}
	}

	public static boolean hasInformation(Holder<Enchantment> enchantment) {
		return id(enchantment) != null;
	}

	public static void append(Holder<Enchantment> enchantment, List<Component> lines) {
		int guiScale = Math.max(1, Minecraft.getInstance().getWindow().getGuiScale());
		int contentMaxWidth = Math.max(1, TOOLTIP_PHYSICAL_MAX_WIDTH / guiScale - TOOLTIP_HORIZONTAL_PADDING);
		append(enchantment, lines, contentMaxWidth);
	}

	public static void append(Holder<Enchantment> enchantment, List<Component> lines, int maxWidth) {
		String id = id(enchantment);
		if (id == null) {
			return;
		}
		appendWrapped(lines, Component.translatable(
			"caphard.tooltip.enchantment.description",
			Component.translatable("caphard.enchantment." + id + ".description")
		), maxWidth);
		appendWrapped(lines, Component.translatable(
			"caphard.tooltip.enchantment.equipment",
			Component.translatable("caphard.enchantment." + id + ".equipment")
		), maxWidth);
	}

	private static String id(Holder<Enchantment> enchantment) {
		return enchantment.unwrapKey()
			.filter(key -> key.identifier().getNamespace().equals("minecraft"))
			.map(key -> key.identifier().getPath())
			.filter(SUPPORTED_ENCHANTMENTS::contains)
			.orElse(null);
	}

	private static void appendWrapped(List<Component> lines, Component text, int maxWidth) {
		String[] words = text.getString().split(" ");
		StringBuilder current = new StringBuilder();
		for (String word : words) {
			String candidate = current.isEmpty() ? word : current + " " + word;
			if (!current.isEmpty() && Minecraft.getInstance().font.width(candidate) > maxWidth) {
				lines.add(Component.literal(current.toString()).withStyle(ChatFormatting.GRAY));
				current.setLength(0);
			}
			if (!current.isEmpty()) {
				current.append(' ');
			}
			current.append(word);
		}
		if (!current.isEmpty()) {
			lines.add(Component.literal(current.toString()).withStyle(ChatFormatting.GRAY));
		}
	}
}
