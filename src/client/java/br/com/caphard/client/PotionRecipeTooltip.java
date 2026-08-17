package br.com.caphard.client;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

public final class PotionRecipeTooltip {
	private static final List<RecipeFamily> RECIPES = List.of(
		family(Items.GOLDEN_CARROT, true, false, Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION),
		family(Items.MAGMA_CREAM, true, false, Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE),
		family(Items.RABBIT_FOOT, true, true, Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING),
		family(Items.TURTLE_HELMET, true, true, Potions.TURTLE_MASTER, Potions.LONG_TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER),
		family(Items.SUGAR, true, true, Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS),
		family(Items.PUFFERFISH, true, false, Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING),
		family(Items.GLISTERING_MELON_SLICE, false, true, Potions.HEALING, Potions.STRONG_HEALING),
		family(Items.SPIDER_EYE, true, true, Potions.POISON, Potions.LONG_POISON, Potions.STRONG_POISON),
		family(Items.GHAST_TEAR, true, true, Potions.REGENERATION, Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION),
		family(Items.BLAZE_POWDER, true, true, Potions.STRENGTH, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH),
		family(Items.PHANTOM_MEMBRANE, true, false, Potions.SLOW_FALLING, Potions.LONG_SLOW_FALLING),
		family(Items.BREEZE_ROD, false, false, Potions.WIND_CHARGED),
		family(Items.SLIME_BLOCK, false, false, Potions.OOZING),
		family(Items.STONE, false, false, Potions.INFESTED),
		family(Items.COBWEB, false, false, Potions.WEAVING)
	);

	private PotionRecipeTooltip() {
	}

	public static void append(ItemStack stack, List<Component> lines) {
		if (!(stack.getItem() instanceof PotionItem)) {
			return;
		}

		PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
		if (contents == null || contents.potion().isEmpty() || !contents.customEffects().isEmpty()) {
			return;
		}

		RecipeFamily recipe = find(contents.potion().get());
		if (recipe == null) {
			return;
		}

		lines.add(Component.empty());
		lines.add(line("caphard.tooltip.potion.ingredient", recipe.ingredient));
		if (recipe.extendable) {
			lines.add(line("caphard.tooltip.potion.extended", Items.REDSTONE));
		}
		lines.add(line("caphard.tooltip.potion.splash", Items.GUNPOWDER));
		if (recipe.amplifiable) {
			lines.add(line("caphard.tooltip.potion.potency", Items.GLOWSTONE_DUST));
		}
	}

	private static RecipeFamily find(Holder<Potion> potion) {
		for (RecipeFamily recipe : RECIPES) {
			if (recipe.matches(potion)) {
				return recipe;
			}
		}
		return null;
	}

	private static Component line(String key, Item ingredient) {
		return Component.translatable(key, ingredientName(ingredient)).withStyle(ChatFormatting.GRAY);
	}

	private static Component ingredientName(Item ingredient) {
		if (ingredient == Items.REDSTONE) {
			return Component.translatable("caphard.tooltip.potion.ingredient.redstone");
		}
		if (ingredient == Items.GLOWSTONE_DUST) {
			return Component.translatable("caphard.tooltip.potion.ingredient.glowstone");
		}
		if (ingredient == Items.GLISTERING_MELON_SLICE) {
			return Component.translatable("caphard.tooltip.potion.ingredient.glistering_melon");
		}
		if (ingredient == Items.BLAZE_POWDER) {
			return Component.translatable("caphard.tooltip.potion.ingredient.blaze");
		}
		if (ingredient == Items.PHANTOM_MEMBRANE) {
			return Component.translatable("caphard.tooltip.potion.ingredient.membrane");
		}
		return Component.translatable(ingredient.getDescriptionId());
	}

	@SafeVarargs
	private static RecipeFamily family(Item ingredient, boolean extendable, boolean amplifiable, Holder<Potion>... potions) {
		return new RecipeFamily(ingredient, extendable, amplifiable, List.of(potions));
	}

	private record RecipeFamily(Item ingredient, boolean extendable, boolean amplifiable, List<Holder<Potion>> potions) {
		private boolean matches(Holder<Potion> potion) {
			return this.potions.contains(potion);
		}
	}
}
