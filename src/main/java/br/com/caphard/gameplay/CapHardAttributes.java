package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.Consumables;

public final class CapHardAttributes {
	public static final int HUNGER = 8;
	public static final float SATURATION = 12.8F;
	public static final float CONSUME_SECONDS = 1.6F;

	// Since Minecraft 26.2, FoodProperties stores the final saturation points.
	private static final FoodProperties CAP_FOOD = new FoodProperties(HUNGER, SATURATION, false);
	private static final Map<Consumable, Consumable> CAP_CONSUMABLES = new ConcurrentHashMap<>();

	private CapHardAttributes() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T overrideComponent(ItemStack stack, DataComponentType<? extends T> type) {
		if (!isCapComponent(type) || !CapHardConfig.isSelected(stack.getItem())) {
			return null;
		}

		if (type == DataComponents.FOOD) {
			return (T) CAP_FOOD;
		}

		if (type == DataComponents.CONSUMABLE) {
			Consumable original = stack.getComponents().get(DataComponents.CONSUMABLE);
			Consumable behavior = original == null ? Consumables.DEFAULT_FOOD : original;
			return (T) CAP_CONSUMABLES.computeIfAbsent(behavior, CapHardAttributes::withCapSpeed);
		}

		return null;
	}

	public static Stream<ConsumableListener> overrideConsumableListeners(
		ItemStack stack,
		Stream<ConsumableListener> originalListeners
	) {
		if (!CapHardConfig.isSelected(stack.getItem())) {
			return originalListeners;
		}

		Stream<ConsumableListener> listenersWithoutVanillaFood = originalListeners
			.filter(listener -> !(listener instanceof FoodProperties));
		return Stream.concat(listenersWithoutVanillaFood, Stream.of(CAP_FOOD));
	}

	public static void applyCakeSlice(
		FoodData foodData,
		int vanillaNutrition,
		float vanillaSaturationModifier
	) {
		if (CapHardConfig.isSelected(Items.CAKE)) {
			foodData.eat(CAP_FOOD);
		} else {
			foodData.eat(vanillaNutrition, vanillaSaturationModifier);
		}
	}

	public static boolean hasOverride(ItemStack stack, DataComponentType<?> type) {
		return isCapComponent(type) && CapHardConfig.isSelected(stack.getItem());
	}

	private static boolean isCapComponent(DataComponentType<?> type) {
		return type == DataComponents.FOOD || type == DataComponents.CONSUMABLE;
	}

	private static Consumable withCapSpeed(Consumable behavior) {
		return new Consumable(
			CONSUME_SECONDS,
			behavior.animation(),
			behavior.sound(),
			behavior.hasConsumeParticles(),
			behavior.onConsumeEffects()
		);
	}

}
