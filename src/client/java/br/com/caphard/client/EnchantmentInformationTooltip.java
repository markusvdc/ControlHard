package br.com.caphard.client;

import java.util.List;
import java.util.Map;
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
	private static final Map<String, List<String>> INCOMPATIBLE_ENCHANTMENTS = Map.ofEntries(
		Map.entry("bane_of_arthropods", List.of("smite", "sharpness")),
		Map.entry("blast_protection", List.of("fire_protection", "projectile_protection", "protection")),
		Map.entry("breach", List.of("density")),
		Map.entry("channeling", List.of("riptide")),
		Map.entry("density", List.of("breach")),
		Map.entry("depth_strider", List.of("frost_walker")),
		Map.entry("fire_protection", List.of("blast_protection", "projectile_protection", "protection")),
		Map.entry("fortune", List.of("silk_touch")),
		Map.entry("frost_walker", List.of("depth_strider")),
		Map.entry("infinity", List.of("mending")),
		Map.entry("loyalty", List.of("riptide")),
		Map.entry("mending", List.of("infinity")),
		Map.entry("multishot", List.of("piercing")),
		Map.entry("piercing", List.of("multishot")),
		Map.entry("projectile_protection", List.of("blast_protection", "fire_protection", "protection")),
		Map.entry("protection", List.of("blast_protection", "fire_protection", "projectile_protection")),
		Map.entry("riptide", List.of("channeling", "loyalty")),
		Map.entry("sharpness", List.of("bane_of_arthropods", "smite")),
		Map.entry("silk_touch", List.of("fortune")),
		Map.entry("smite", List.of("bane_of_arthropods", "sharpness"))
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
		List<String> incompatible = INCOMPATIBLE_ENCHANTMENTS.get(id);
		if (incompatible != null) {
			appendWrapped(lines, Component.translatable(
				"caphard.tooltip.enchantment.incompatible",
				joinedEnchantmentNames(incompatible)
			), maxWidth);
		}
	}

	private static Component joinedEnchantmentNames(List<String> enchantments) {
		Component result = CommonComponents.EMPTY;
		for (int index = 0; index < enchantments.size(); index++) {
			if (index > 0) {
				String separatorKey = index == enchantments.size() - 1
					? "caphard.tooltip.enchantment.final_separator"
					: "caphard.tooltip.enchantment.separator";
				result = result.copy().append(Component.translatable(separatorKey));
			}
			result = result.copy().append(Component.translatable("enchantment.minecraft." + enchantments.get(index)));
		}
		return result;
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
