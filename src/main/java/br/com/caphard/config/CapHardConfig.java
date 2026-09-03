package br.com.caphard.config;

import br.com.caphard.gameplay.HardRule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class CapHardConfig {
	private static final int CONFIG_VERSION = 20;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("caphard.json");
	private static final Set<String> ALLOWED_FOODS = Set.of(
		"minecraft:cooked_cod",
		"minecraft:cooked_porkchop",
		"minecraft:cooked_mutton",
		"minecraft:cooked_rabbit",
		"minecraft:cooked_chicken",
		"minecraft:cooked_salmon",
		"minecraft:mushroom_stew",
		"minecraft:rabbit_stew",
		"minecraft:suspicious_stew",
		"minecraft:beetroot_soup",
		"minecraft:baked_potato",
		"minecraft:cookie",
		"minecraft:cake",
		"minecraft:honey_bottle",
		"minecraft:apple",
		"minecraft:bread",
		"minecraft:pumpkin_pie",
		"minecraft:glow_berries",
		"minecraft:sweet_berries",
		"minecraft:popped_chorus_fruit",
		"minecraft:dried_kelp"
	);

	private static volatile Set<String> selectedFoods = ALLOWED_FOODS;
	private static volatile Set<String> enabledRules = allRuleIds();
	private static volatile boolean consumeContainer;
	private static volatile boolean showFoodProperties;
	private static volatile boolean markHiddenInformation;
	private static volatile boolean preventRottenFleshWolfFeeding;
	private static volatile boolean preventPrimaryFoodConsumption;
	private static volatile boolean horseIgnoresLeaves;
	private static volatile boolean fasterLeafDecay;
	private static volatile boolean increasedSaplingBeeNestChance;
	private static volatile boolean beesSurviveStinging;
	private static volatile boolean showStatusEffectPanel;
	private static volatile boolean showPotionRecipes;
	private static volatile boolean fortuneWheatHarvest;
	private static volatile boolean fortuneBeetrootHarvest;
	private static volatile boolean protectTilledSoil;
	private static volatile boolean sortSpecialItemsByDescription;
	private static volatile boolean retainHalfExperienceOnDeath;
	private static volatile boolean infinityWithoutArrow;
	private static volatile boolean showEnchantmentInformation;
	private static volatile boolean spyglassTreasureVision;
	private static volatile boolean uniqueTreasureMaps;
	private static volatile boolean uprightCustomNames;
	private static volatile boolean magneticDropSafeguard;
	private static volatile boolean lodestoneCompassTeleport;
	private static volatile boolean safeChorusTeleport;
	private static volatile boolean separateRecipeBookStates;
	private static volatile boolean inventoryRecipeBookOpen;
	private static volatile boolean craftingTableRecipeBookOpen;

	private CapHardConfig() {
	}

	public static synchronized void load() {
		if (!Files.exists(CONFIG_PATH)) {
			applyFirstInstallDefaults();
			return;
		}

		try {
			String json = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
			ConfigData data = GSON.fromJson(json, ConfigData.class);
			selectedFoods = loadSelectedFoods(data);
			enabledRules = loadEnabledRules(data);
			consumeContainer = data != null && data.consumeContainer;
			showFoodProperties = data != null && Boolean.TRUE.equals(data.showFoodProperties);
			markHiddenInformation = data != null && Boolean.TRUE.equals(data.markHiddenInformation);
			preventRottenFleshWolfFeeding = data != null && data.preventRottenFleshWolfFeeding;
			preventPrimaryFoodConsumption = data != null && data.preventPrimaryFoodConsumption;
			horseIgnoresLeaves = data != null && Boolean.TRUE.equals(data.horseIgnoresLeaves);
			fasterLeafDecay = data != null && Boolean.TRUE.equals(data.fasterLeafDecay);
			increasedSaplingBeeNestChance = data != null && Boolean.TRUE.equals(data.increasedSaplingBeeNestChance);
			beesSurviveStinging = data != null && Boolean.TRUE.equals(data.beesSurviveStinging);
			showStatusEffectPanel = data != null && Boolean.TRUE.equals(data.showStatusEffectPanel);
			showPotionRecipes = data != null && Boolean.TRUE.equals(data.showPotionRecipes);
			fortuneWheatHarvest = data != null && Boolean.TRUE.equals(data.fortuneWheatHarvest);
			fortuneBeetrootHarvest = data != null && Boolean.TRUE.equals(data.fortuneBeetrootHarvest);
			protectTilledSoil = data != null && Boolean.TRUE.equals(data.protectTilledSoil);
			sortSpecialItemsByDescription = data != null && Boolean.TRUE.equals(data.sortSpecialItemsByDescription);
			retainHalfExperienceOnDeath = data != null && Boolean.TRUE.equals(data.retainHalfExperienceOnDeath);
			infinityWithoutArrow = data != null && Boolean.TRUE.equals(data.infinityWithoutArrow);
			showEnchantmentInformation = data != null && Boolean.TRUE.equals(data.showEnchantmentInformation);
			spyglassTreasureVision = data != null && Boolean.TRUE.equals(data.spyglassTreasureVision);
			uniqueTreasureMaps = data != null && Boolean.TRUE.equals(data.uniqueTreasureMaps);
			uprightCustomNames = data != null && Boolean.TRUE.equals(data.uprightCustomNames);
			magneticDropSafeguard = data != null && Boolean.TRUE.equals(data.magneticDropSafeguard);
			lodestoneCompassTeleport = data != null && Boolean.TRUE.equals(data.lodestoneCompassTeleport);
			safeChorusTeleport = data != null && Boolean.TRUE.equals(data.safeChorusTeleport);
			separateRecipeBookStates = data != null && Boolean.TRUE.equals(data.separateRecipeBookStates);
			inventoryRecipeBookOpen = data != null && Boolean.TRUE.equals(data.inventoryRecipeBookOpen);
			craftingTableRecipeBookOpen = data != null && Boolean.TRUE.equals(data.craftingTableRecipeBookOpen);
		} catch (IOException | JsonParseException exception) {
			selectedFoods = Set.of();
			enabledRules = allRuleIds();
			consumeContainer = false;
			showFoodProperties = false;
			markHiddenInformation = false;
			preventRottenFleshWolfFeeding = false;
			preventPrimaryFoodConsumption = false;
			horseIgnoresLeaves = false;
			fasterLeafDecay = false;
			increasedSaplingBeeNestChance = false;
			beesSurviveStinging = false;
			showStatusEffectPanel = false;
			showPotionRecipes = false;
			fortuneWheatHarvest = false;
			fortuneBeetrootHarvest = false;
			protectTilledSoil = false;
			sortSpecialItemsByDescription = false;
			retainHalfExperienceOnDeath = false;
			infinityWithoutArrow = false;
			showEnchantmentInformation = false;
			spyglassTreasureVision = false;
			uniqueTreasureMaps = false;
			uprightCustomNames = false;
			magneticDropSafeguard = false;
			lodestoneCompassTeleport = false;
			safeChorusTeleport = false;
			separateRecipeBookStates = false;
			inventoryRecipeBookOpen = false;
			craftingTableRecipeBookOpen = false;
		}
	}

	public static synchronized boolean saveSelection(Collection<Identifier> itemIds) {
		Set<String> sanitized = sanitize(itemIds.stream().map(Identifier::toString).toList());
		if (!save(
			sanitized,
			enabledRules,
			consumeContainer,
			showFoodProperties,
			markHiddenInformation,
			preventRottenFleshWolfFeeding,
			preventPrimaryFoodConsumption,
			horseIgnoresLeaves,
			fasterLeafDecay,
			increasedSaplingBeeNestChance,
			beesSurviveStinging,
			showStatusEffectPanel,
			showPotionRecipes,
			fortuneWheatHarvest,
			fortuneBeetrootHarvest,
			protectTilledSoil,
			sortSpecialItemsByDescription,
			retainHalfExperienceOnDeath,
			infinityWithoutArrow,
			showEnchantmentInformation,
			spyglassTreasureVision,
			uniqueTreasureMaps,
			uprightCustomNames,
			magneticDropSafeguard,
			lodestoneCompassTeleport,
			safeChorusTeleport,
			separateRecipeBookStates,
			inventoryRecipeBookOpen,
			craftingTableRecipeBookOpen
		)) {
			return false;
		}
		selectedFoods = sanitized;
		return true;
	}

	public static synchronized boolean saveGlobalOptions(
		boolean newConsumeContainer,
		boolean newShowFoodProperties,
		boolean newMarkHiddenInformation,
		boolean newPreventRottenFleshWolfFeeding,
		boolean newPreventPrimaryFoodConsumption,
		boolean newHorseIgnoresLeaves,
		boolean newFasterLeafDecay,
		boolean newIncreasedSaplingBeeNestChance,
		boolean newBeesSurviveStinging,
		boolean newShowStatusEffectPanel,
		boolean newShowPotionRecipes,
		boolean newFortuneWheatHarvest,
		boolean newFortuneBeetrootHarvest,
		boolean newProtectTilledSoil,
		boolean newSortSpecialItemsByDescription,
		boolean newRetainHalfExperienceOnDeath,
		boolean newInfinityWithoutArrow,
		boolean newShowEnchantmentInformation,
		boolean newSpyglassTreasureVision,
		boolean newUniqueTreasureMaps,
		boolean newUprightCustomNames,
		boolean newMagneticDropSafeguard,
		boolean newLodestoneCompassTeleport,
		boolean newSafeChorusTeleport,
		boolean newSeparateRecipeBookStates
	) {
		if (!save(
			selectedFoods,
			enabledRules,
			newConsumeContainer,
			newShowFoodProperties,
			newMarkHiddenInformation,
			newPreventRottenFleshWolfFeeding,
			newPreventPrimaryFoodConsumption,
			newHorseIgnoresLeaves,
			newFasterLeafDecay,
			newIncreasedSaplingBeeNestChance,
			newBeesSurviveStinging,
			newShowStatusEffectPanel,
			newShowPotionRecipes,
			newFortuneWheatHarvest,
			newFortuneBeetrootHarvest,
			newProtectTilledSoil,
			newSortSpecialItemsByDescription,
			newRetainHalfExperienceOnDeath,
			newInfinityWithoutArrow,
			newShowEnchantmentInformation,
			newSpyglassTreasureVision,
			newUniqueTreasureMaps,
			newUprightCustomNames,
			newMagneticDropSafeguard,
			newLodestoneCompassTeleport,
			newSafeChorusTeleport,
			newSeparateRecipeBookStates,
			inventoryRecipeBookOpen,
			craftingTableRecipeBookOpen
		)) {
			return false;
		}
		consumeContainer = newConsumeContainer;
		showFoodProperties = newShowFoodProperties;
		markHiddenInformation = newMarkHiddenInformation;
		preventRottenFleshWolfFeeding = newPreventRottenFleshWolfFeeding;
		preventPrimaryFoodConsumption = newPreventPrimaryFoodConsumption;
		horseIgnoresLeaves = newHorseIgnoresLeaves;
		fasterLeafDecay = newFasterLeafDecay;
		increasedSaplingBeeNestChance = newIncreasedSaplingBeeNestChance;
		beesSurviveStinging = newBeesSurviveStinging;
		showStatusEffectPanel = newShowStatusEffectPanel;
		showPotionRecipes = newShowPotionRecipes;
		fortuneWheatHarvest = newFortuneWheatHarvest;
		fortuneBeetrootHarvest = newFortuneBeetrootHarvest;
		protectTilledSoil = newProtectTilledSoil;
		sortSpecialItemsByDescription = newSortSpecialItemsByDescription;
		retainHalfExperienceOnDeath = newRetainHalfExperienceOnDeath;
		infinityWithoutArrow = newInfinityWithoutArrow;
		showEnchantmentInformation = newShowEnchantmentInformation;
		spyglassTreasureVision = newSpyglassTreasureVision;
		uniqueTreasureMaps = newUniqueTreasureMaps;
		uprightCustomNames = newUprightCustomNames;
		magneticDropSafeguard = newMagneticDropSafeguard;
		lodestoneCompassTeleport = newLodestoneCompassTeleport;
		safeChorusTeleport = newSafeChorusTeleport;
		separateRecipeBookStates = newSeparateRecipeBookStates;
		return true;
	}

	public static synchronized boolean saveRules(Collection<HardRule> rules) {
		Set<String> sanitized = sanitizeRules(rules);
		if (!save(
			selectedFoods,
			sanitized,
			consumeContainer,
			showFoodProperties,
			markHiddenInformation,
			preventRottenFleshWolfFeeding,
			preventPrimaryFoodConsumption,
			horseIgnoresLeaves,
			fasterLeafDecay,
			increasedSaplingBeeNestChance,
			beesSurviveStinging,
			showStatusEffectPanel,
			showPotionRecipes,
			fortuneWheatHarvest,
			fortuneBeetrootHarvest,
			protectTilledSoil,
			sortSpecialItemsByDescription,
			retainHalfExperienceOnDeath,
			infinityWithoutArrow,
			showEnchantmentInformation,
			spyglassTreasureVision,
			uniqueTreasureMaps,
			uprightCustomNames,
			magneticDropSafeguard,
			lodestoneCompassTeleport,
			safeChorusTeleport,
			separateRecipeBookStates,
			inventoryRecipeBookOpen,
			craftingTableRecipeBookOpen
		)) {
			return false;
		}
		enabledRules = sanitized;
		return true;
	}

	public static boolean consumeContainer() {
		return consumeContainer;
	}

	public static boolean showFoodProperties() {
		return showFoodProperties;
	}

	public static boolean markHiddenInformation() {
		return markHiddenInformation;
	}

	public static boolean preventRottenFleshWolfFeeding() {
		return preventRottenFleshWolfFeeding;
	}

	public static boolean preventPrimaryFoodConsumption() {
		return preventPrimaryFoodConsumption;
	}

	public static boolean horseIgnoresLeaves() {
		return horseIgnoresLeaves;
	}

	public static boolean fasterLeafDecay() {
		return fasterLeafDecay;
	}

	public static boolean increasedSaplingBeeNestChance() {
		return increasedSaplingBeeNestChance;
	}

	public static boolean beesSurviveStinging() {
		return beesSurviveStinging;
	}

	public static boolean showStatusEffectPanel() {
		return showStatusEffectPanel;
	}

	public static boolean showPotionRecipes() {
		return showPotionRecipes;
	}

	public static boolean fortuneWheatHarvest() {
		return fortuneWheatHarvest;
	}

	public static boolean fortuneBeetrootHarvest() {
		return fortuneBeetrootHarvest;
	}

	public static boolean protectTilledSoil() {
		return protectTilledSoil;
	}

	public static boolean sortSpecialItemsByDescription() {
		return sortSpecialItemsByDescription;
	}

	public static boolean retainHalfExperienceOnDeath() {
		return retainHalfExperienceOnDeath;
	}

	public static boolean infinityWithoutArrow() {
		return infinityWithoutArrow;
	}

	public static boolean showEnchantmentInformation() {
		return showEnchantmentInformation;
	}

	public static boolean spyglassTreasureVision() {
		return spyglassTreasureVision;
	}

	public static boolean uniqueTreasureMaps() {
		return uniqueTreasureMaps;
	}

	public static boolean uprightCustomNames() {
		return uprightCustomNames;
	}

	public static boolean magneticDropSafeguard() {
		return magneticDropSafeguard;
	}

	public static boolean lodestoneCompassTeleport() {
		return lodestoneCompassTeleport;
	}

	public static boolean safeChorusTeleport() {
		return safeChorusTeleport;
	}

	public static boolean separateRecipeBookStates() {
		return separateRecipeBookStates;
	}

	public static boolean inventoryRecipeBookOpen() {
		return inventoryRecipeBookOpen;
	}

	public static boolean craftingTableRecipeBookOpen() {
		return craftingTableRecipeBookOpen;
	}

	public static synchronized boolean saveRecipeBookState(boolean inventoryScreen, boolean open) {
		boolean newInventoryState = inventoryScreen ? open : inventoryRecipeBookOpen;
		boolean newCraftingTableState = inventoryScreen ? craftingTableRecipeBookOpen : open;
		if (!save(
			selectedFoods,
			enabledRules,
			consumeContainer,
			showFoodProperties,
			markHiddenInformation,
			preventRottenFleshWolfFeeding,
			preventPrimaryFoodConsumption,
			horseIgnoresLeaves,
			fasterLeafDecay,
			increasedSaplingBeeNestChance,
			beesSurviveStinging,
			showStatusEffectPanel,
			showPotionRecipes,
			fortuneWheatHarvest,
			fortuneBeetrootHarvest,
			protectTilledSoil,
			sortSpecialItemsByDescription,
			retainHalfExperienceOnDeath,
			infinityWithoutArrow,
			showEnchantmentInformation,
			spyglassTreasureVision,
			uniqueTreasureMaps,
			uprightCustomNames,
			magneticDropSafeguard,
			lodestoneCompassTeleport,
			safeChorusTeleport,
			separateRecipeBookStates,
			newInventoryState,
			newCraftingTableState
		)) {
			return false;
		}
		inventoryRecipeBookOpen = newInventoryState;
		craftingTableRecipeBookOpen = newCraftingTableState;
		return true;
	}

	public static boolean isRuleEnabled(HardRule rule) {
		return enabledRules.contains(rule.id());
	}

	public static boolean isSelected(Item item) {
		return selectedFoods.contains(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	public static boolean isAllowed(Item item) {
		return ALLOWED_FOODS.contains(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	public static Set<Identifier> selectedIds() {
		LinkedHashSet<Identifier> ids = new LinkedHashSet<>();
		for (String id : selectedFoods) {
			ids.add(Identifier.parse(id));
		}
		return Set.copyOf(ids);
	}

	private static boolean save(
		Set<String> foods,
		Set<String> rules,
		boolean shouldConsumeContainer,
		boolean shouldShowFoodProperties,
		boolean shouldMarkHiddenInformation,
		boolean shouldPreventRottenFleshWolfFeeding,
		boolean shouldPreventPrimaryFoodConsumption,
		boolean shouldHorseIgnoreLeaves,
		boolean shouldUseFasterLeafDecay,
		boolean shouldIncreaseSaplingBeeNestChance,
		boolean shouldBeesSurviveStinging,
		boolean shouldShowStatusEffectPanel,
		boolean shouldShowPotionRecipes,
		boolean shouldUseFortuneWheatHarvest,
		boolean shouldUseFortuneBeetrootHarvest,
		boolean shouldProtectTilledSoil,
		boolean shouldSortSpecialItemsByDescription,
		boolean shouldRetainHalfExperienceOnDeath,
		boolean shouldUseInfinityWithoutArrow,
		boolean shouldShowEnchantmentInformation,
		boolean shouldUseSpyglassTreasureVision,
		boolean shouldUseUniqueTreasureMaps,
		boolean shouldUseUprightCustomNames,
		boolean shouldUseMagneticDropSafeguard,
		boolean shouldUseLodestoneCompassTeleport,
		boolean shouldUseSafeChorusTeleport,
		boolean shouldSeparateRecipeBookStates,
		boolean shouldOpenInventoryRecipeBook,
		boolean shouldOpenCraftingTableRecipeBook
	) {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Path temporaryPath = CONFIG_PATH.resolveSibling(CONFIG_PATH.getFileName() + ".tmp");
			ConfigData data = new ConfigData(
				CONFIG_VERSION,
				foods.stream().sorted().toList(),
				rules.stream().sorted().toList(),
				shouldConsumeContainer,
				shouldShowFoodProperties,
				shouldMarkHiddenInformation,
				shouldPreventRottenFleshWolfFeeding,
				shouldPreventPrimaryFoodConsumption,
				shouldHorseIgnoreLeaves,
				shouldUseFasterLeafDecay,
				shouldIncreaseSaplingBeeNestChance,
				shouldBeesSurviveStinging,
				shouldShowStatusEffectPanel,
				shouldShowPotionRecipes,
				shouldUseFortuneWheatHarvest,
				shouldUseFortuneBeetrootHarvest,
				shouldProtectTilledSoil,
				shouldSortSpecialItemsByDescription,
				shouldRetainHalfExperienceOnDeath,
				shouldUseInfinityWithoutArrow,
				shouldShowEnchantmentInformation,
				shouldUseSpyglassTreasureVision,
				shouldUseUniqueTreasureMaps,
				shouldUseUprightCustomNames,
				shouldUseMagneticDropSafeguard,
				shouldUseLodestoneCompassTeleport,
				shouldUseSafeChorusTeleport,
				shouldSeparateRecipeBookStates,
				shouldOpenInventoryRecipeBook,
				shouldOpenCraftingTableRecipeBook
			);
			Files.writeString(temporaryPath, GSON.toJson(data), StandardCharsets.UTF_8);
			Files.move(temporaryPath, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException exception) {
			return false;
		}
	}

	private static Set<String> sanitize(Collection<String> ids) {
		LinkedHashSet<String> sanitized = new LinkedHashSet<>();
		if (ids != null) {
			for (String id : ids) {
				if (id != null && ALLOWED_FOODS.contains(id)) {
					sanitized.add(id);
				}
			}
		}
		return Set.copyOf(sanitized);
	}

	private static Set<String> sanitizeRules(Collection<HardRule> rules) {
		LinkedHashSet<String> sanitized = new LinkedHashSet<>();
		if (rules != null) {
			for (HardRule rule : rules) {
				if (rule != null) {
					sanitized.add(rule.id());
				}
			}
		}
		return Set.copyOf(sanitized);
	}

	private static Set<String> loadEnabledRules(ConfigData data) {
		if (data == null || data.enabledRules == null) {
			return allRuleIds();
		}
		LinkedHashSet<String> rules = new LinkedHashSet<>();
		for (String id : data.enabledRules) {
			if (HardRule.byId(id) != null) {
				rules.add(id);
			}
		}
		if (data.version == null || data.version < 8) {
			rules.add(HardRule.PHANTOM_SPEED.id());
		}
		if (data.version == null || data.version < 10) {
			rules.add(HardRule.WITCH_SLOWNESS_DURATION.id());
		}
		if (data.version == null || data.version < 13) {
			rules.add(HardRule.CAPPED_EXPERIENCE_COST.id());
		}
		return Set.copyOf(rules);
	}

	private static Set<String> allRuleIds() {
		LinkedHashSet<String> rules = new LinkedHashSet<>();
		for (HardRule rule : HardRule.values()) {
			rules.add(rule.id());
		}
		return Set.copyOf(rules);
	}

	private static Set<String> loadSelectedFoods(ConfigData data) {
		if (data == null || data.selectedFoods == null) {
			return ALLOWED_FOODS;
		}

		LinkedHashSet<String> foods = new LinkedHashSet<>(sanitize(data.selectedFoods));
		if (data.version == null || data.version < CONFIG_VERSION) {
			if (data.version == null || data.version < 2) {
				foods.add("minecraft:glow_berries");
			}
			if (data.version == null || data.version < 4) {
				foods.add("minecraft:suspicious_stew");
			}
			if (data.version == null || data.version < 5) {
				foods.add("minecraft:popped_chorus_fruit");
				foods.add("minecraft:dried_kelp");
			}
			foods.add("minecraft:sweet_berries");
		}
		return Set.copyOf(foods);
	}

	private static void applyFirstInstallDefaults() {
		selectedFoods = ALLOWED_FOODS;
		enabledRules = allRuleIds();
		consumeContainer = false;
		showFoodProperties = false;
		markHiddenInformation = false;
		preventRottenFleshWolfFeeding = false;
		preventPrimaryFoodConsumption = false;
		horseIgnoresLeaves = false;
		fasterLeafDecay = false;
		increasedSaplingBeeNestChance = false;
		beesSurviveStinging = false;
		showStatusEffectPanel = false;
		showPotionRecipes = false;
		fortuneWheatHarvest = false;
		fortuneBeetrootHarvest = false;
		protectTilledSoil = false;
		sortSpecialItemsByDescription = false;
		retainHalfExperienceOnDeath = false;
		infinityWithoutArrow = false;
		showEnchantmentInformation = false;
		spyglassTreasureVision = false;
		uniqueTreasureMaps = false;
		uprightCustomNames = false;
		magneticDropSafeguard = false;
		lodestoneCompassTeleport = false;
		safeChorusTeleport = false;
		separateRecipeBookStates = false;
		inventoryRecipeBookOpen = false;
		craftingTableRecipeBookOpen = false;
	}

	private record ConfigData(
		Integer version,
		List<String> selectedFoods,
		List<String> enabledRules,
		boolean consumeContainer,
		Boolean showFoodProperties,
		Boolean markHiddenInformation,
		boolean preventRottenFleshWolfFeeding,
		boolean preventPrimaryFoodConsumption,
		Boolean horseIgnoresLeaves,
		Boolean fasterLeafDecay,
		Boolean increasedSaplingBeeNestChance,
		Boolean beesSurviveStinging,
		Boolean showStatusEffectPanel,
		Boolean showPotionRecipes,
		Boolean fortuneWheatHarvest,
		Boolean fortuneBeetrootHarvest,
		Boolean protectTilledSoil,
		Boolean sortSpecialItemsByDescription,
		Boolean retainHalfExperienceOnDeath,
		Boolean infinityWithoutArrow,
		Boolean showEnchantmentInformation,
		Boolean spyglassTreasureVision,
		Boolean uniqueTreasureMaps,
		Boolean uprightCustomNames,
		Boolean magneticDropSafeguard,
		Boolean lodestoneCompassTeleport,
		Boolean safeChorusTeleport,
		Boolean separateRecipeBookStates,
		Boolean inventoryRecipeBookOpen,
		Boolean craftingTableRecipeBookOpen
	) {
	}
}
