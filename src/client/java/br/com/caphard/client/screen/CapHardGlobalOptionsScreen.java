package br.com.caphard.client.screen;

import br.com.caphard.client.screen.component.ActionButtons;
import br.com.caphard.client.screen.component.CapBasePanel;
import br.com.caphard.client.screen.component.CategoryDivider;
import br.com.caphard.client.screen.component.GlobalOptionEntry;
import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CapHardGlobalOptionsScreen extends Screen {
	private static final int MAX_CONTENT_WIDTH = 540;
	private static final int SIDE_MARGIN = 16;
	private static final int OPTIONS_TOP = 137;
	private static final int OPTION_HEIGHT = 30;
	private static final int VISIBLE_ROW_COUNT = 16;
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLLBAR_GAP = 6;

	private final Screen parent;
	private final CapBasePanel basePanel = new CapBasePanel();
	private GlobalOptionEntry fortuneWheatHarvestEntry;
	private GlobalOptionEntry fortuneBeetrootHarvestEntry;
	private GlobalOptionEntry protectTilledSoilEntry;
	private GlobalOptionEntry sortSpecialItemsByDescriptionEntry;
	private GlobalOptionEntry retainHalfExperienceOnDeathEntry;
	private GlobalOptionEntry infinityWithoutArrowEntry;
	private GlobalOptionEntry showEnchantmentInformationEntry;
	private GlobalOptionEntry spyglassTreasureVisionEntry;
	private GlobalOptionEntry uniqueTreasureMapsEntry;
	private GlobalOptionEntry uprightCustomNamesEntry;
	private GlobalOptionEntry magneticDropSafeguardEntry;
	private GlobalOptionEntry lodestoneCompassTeleportEntry;
	private GlobalOptionEntry safeChorusTeleportEntry;
	private GlobalOptionEntry clearWeatherLightingEntry;
	private GlobalOptionEntry separateRecipeBookStatesEntry;
	private boolean fortuneWheatHarvest;
	private boolean fortuneBeetrootHarvest;
	private boolean protectTilledSoil;
	private boolean sortSpecialItemsByDescription;
	private boolean retainHalfExperienceOnDeath;
	private boolean infinityWithoutArrow;
	private boolean showEnchantmentInformation;
	private boolean spyglassTreasureVision;
	private boolean uniqueTreasureMaps;
	private boolean uprightCustomNames;
	private boolean magneticDropSafeguard;
	private boolean lodestoneCompassTeleport;
	private boolean safeChorusTeleport;
	private boolean clearWeatherLighting;
	private boolean separateRecipeBookStates;
	private Component status = Component.empty();
	private int statusColor = 0xFF9CD67A;

	public CapHardGlobalOptionsScreen(Screen parent) {
		super(Component.translatable("caphard.options.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;
		int buttonY = this.height - 36;
		int rowHeight = Math.min(OPTION_HEIGHT, (buttonY - 12 - OPTIONS_TOP) / VISIBLE_ROW_COUNT);
		this.fortuneWheatHarvest = CapHardConfig.fortuneWheatHarvest();
		this.fortuneBeetrootHarvest = CapHardConfig.fortuneBeetrootHarvest();
		this.protectTilledSoil = CapHardConfig.protectTilledSoil();
		this.sortSpecialItemsByDescription = CapHardConfig.sortSpecialItemsByDescription();
		this.retainHalfExperienceOnDeath = CapHardConfig.retainHalfExperienceOnDeath();
		this.infinityWithoutArrow = CapHardConfig.infinityWithoutArrow();
		this.showEnchantmentInformation = CapHardConfig.showEnchantmentInformation();
		this.spyglassTreasureVision = CapHardConfig.spyglassTreasureVision();
		this.uniqueTreasureMaps = CapHardConfig.uniqueTreasureMaps();
		this.uprightCustomNames = CapHardConfig.uprightCustomNames();
		this.magneticDropSafeguard = CapHardConfig.magneticDropSafeguard();
		this.lodestoneCompassTeleport = CapHardConfig.lodestoneCompassTeleport();
		this.safeChorusTeleport = CapHardConfig.safeChorusTeleport();
		this.clearWeatherLighting = CapHardConfig.clearWeatherLighting();
		this.separateRecipeBookStates = CapHardConfig.separateRecipeBookStates();

		this.fortuneWheatHarvestEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.fortune_wheat_harvest"),
			"caphard.options.fortune_wheat_harvest",
			this.fortuneWheatHarvest,
			selected -> this.fortuneWheatHarvest = selected
		);
		this.fortuneBeetrootHarvestEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.fortune_beetroot_harvest"),
			"caphard.options.fortune_beetroot_harvest",
			this.fortuneBeetrootHarvest,
			selected -> this.fortuneBeetrootHarvest = selected
		);
		this.protectTilledSoilEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.protect_tilled_soil"),
			"caphard.options.protect_tilled_soil",
			this.protectTilledSoil,
			selected -> this.protectTilledSoil = selected
		);
		this.sortSpecialItemsByDescriptionEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.sort_special_items_by_description"),
			"caphard.options.sort_special_items_by_description",
			this.sortSpecialItemsByDescription,
			selected -> this.sortSpecialItemsByDescription = selected
		);
		this.retainHalfExperienceOnDeathEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.retain_half_experience_on_death"),
			"caphard.options.retain_half_experience_on_death",
			this.retainHalfExperienceOnDeath,
			selected -> this.retainHalfExperienceOnDeath = selected
		);
		this.infinityWithoutArrowEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.infinity_without_arrow"),
			"caphard.options.infinity_without_arrow",
			this.infinityWithoutArrow,
			selected -> this.infinityWithoutArrow = selected
		);
		this.showEnchantmentInformationEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.show_enchantment_information"),
			"caphard.options.show_enchantment_information",
			this.showEnchantmentInformation,
			selected -> this.showEnchantmentInformation = selected
		);
		this.spyglassTreasureVisionEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.spyglass_treasure_vision"),
			"caphard.options.spyglass_treasure_vision",
			this.spyglassTreasureVision,
			selected -> this.spyglassTreasureVision = selected
		);
		this.uniqueTreasureMapsEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.unique_treasure_maps"),
			"caphard.options.unique_treasure_maps",
			this.uniqueTreasureMaps,
			selected -> this.uniqueTreasureMaps = selected
		);
		this.uprightCustomNamesEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.upright_custom_names"),
			"caphard.options.upright_custom_names",
			this.uprightCustomNames,
			selected -> this.uprightCustomNames = selected
		);
		this.magneticDropSafeguardEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.magnetic_drop_safeguard"),
			"caphard.options.magnetic_drop_safeguard",
			this.magneticDropSafeguard,
			selected -> this.magneticDropSafeguard = selected
		);
		this.lodestoneCompassTeleportEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.lodestone_compass_teleport"),
			"caphard.options.lodestone_compass_teleport",
			this.lodestoneCompassTeleport,
			selected -> this.lodestoneCompassTeleport = selected
		);
		this.safeChorusTeleportEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.safe_chorus_teleport"),
			"caphard.options.safe_chorus_teleport",
			this.safeChorusTeleport,
			selected -> this.safeChorusTeleport = selected
		);
		this.clearWeatherLightingEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.clear_weather_lighting"),
			"caphard.options.clear_weather_lighting",
			this.clearWeatherLighting,
			selected -> this.clearWeatherLighting = selected
		);
		this.separateRecipeBookStatesEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.separate_recipe_book_states"),
			"caphard.options.separate_recipe_book_states",
			this.separateRecipeBookStates,
			selected -> this.separateRecipeBookStates = selected
		);
		int row = 0;
		this.addRenderableWidget(new CategoryDivider(left, OPTIONS_TOP + rowHeight * row++, contentWidth, rowHeight,
			Component.translatable("caphard.options.category.quality")));
		this.sortSpecialItemsByDescriptionEntry.setHeight(rowHeight);
		this.sortSpecialItemsByDescriptionEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.sortSpecialItemsByDescriptionEntry);
		row++;
		this.showEnchantmentInformationEntry.setHeight(rowHeight);
		this.showEnchantmentInformationEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.showEnchantmentInformationEntry);
		row++;
		this.uprightCustomNamesEntry.setHeight(rowHeight);
		this.uprightCustomNamesEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.uprightCustomNamesEntry);
		row++;
		this.separateRecipeBookStatesEntry.setHeight(rowHeight);
		this.separateRecipeBookStatesEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.separateRecipeBookStatesEntry);
		row++;
		this.retainHalfExperienceOnDeathEntry.setHeight(rowHeight);
		this.retainHalfExperienceOnDeathEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.retainHalfExperienceOnDeathEntry);
		row++;
		this.infinityWithoutArrowEntry.setHeight(rowHeight);
		this.infinityWithoutArrowEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.infinityWithoutArrowEntry);
		row++;
		this.magneticDropSafeguardEntry.setHeight(rowHeight);
		this.magneticDropSafeguardEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.magneticDropSafeguardEntry);
		row++;
		this.lodestoneCompassTeleportEntry.setHeight(rowHeight);
		this.lodestoneCompassTeleportEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.lodestoneCompassTeleportEntry);
		row++;
		this.safeChorusTeleportEntry.setHeight(rowHeight);
		this.safeChorusTeleportEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.safeChorusTeleportEntry);
		row++;
		this.clearWeatherLightingEntry.setHeight(rowHeight);
		this.clearWeatherLightingEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.clearWeatherLightingEntry);
		row++;
		this.fortuneWheatHarvestEntry.setHeight(rowHeight);
		this.fortuneWheatHarvestEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.fortuneWheatHarvestEntry);
		row++;
		this.fortuneBeetrootHarvestEntry.setHeight(rowHeight);
		this.fortuneBeetrootHarvestEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.fortuneBeetrootHarvestEntry);
		row++;
		this.protectTilledSoilEntry.setHeight(rowHeight);
		this.protectTilledSoilEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.protectTilledSoilEntry);
		row++;
		this.spyglassTreasureVisionEntry.setHeight(rowHeight);
		this.spyglassTreasureVisionEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.spyglassTreasureVisionEntry);
		row++;
		this.uniqueTreasureMapsEntry.setHeight(rowHeight);
		this.uniqueTreasureMapsEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.uniqueTreasureMapsEntry);

		ActionButtons actionButtons = new ActionButtons(
			left, buttonY, contentWidth, this::onClose, () -> { },
			this::toggleAllOptions, this::applyOptions, true
		);
		actionButtons.addTo(this::addRenderableWidget);
	}

	private void toggleAllOptions() {
		boolean selectAll = !this.sortSpecialItemsByDescription || !this.showEnchantmentInformation || !this.uprightCustomNames || !this.separateRecipeBookStates || !this.retainHalfExperienceOnDeath || !this.infinityWithoutArrow || !this.magneticDropSafeguard || !this.lodestoneCompassTeleport || !this.safeChorusTeleport || !this.clearWeatherLighting || !this.fortuneWheatHarvest || !this.fortuneBeetrootHarvest || !this.protectTilledSoil || !this.spyglassTreasureVision || !this.uniqueTreasureMaps;
		this.sortSpecialItemsByDescriptionEntry.setSelected(selectAll);
		this.showEnchantmentInformationEntry.setSelected(selectAll);
		this.uprightCustomNamesEntry.setSelected(selectAll);
		this.separateRecipeBookStatesEntry.setSelected(selectAll);
		this.retainHalfExperienceOnDeathEntry.setSelected(selectAll);
		this.infinityWithoutArrowEntry.setSelected(selectAll);
		this.magneticDropSafeguardEntry.setSelected(selectAll);
		this.lodestoneCompassTeleportEntry.setSelected(selectAll);
		this.safeChorusTeleportEntry.setSelected(selectAll);
		this.clearWeatherLightingEntry.setSelected(selectAll);
		this.fortuneWheatHarvestEntry.setSelected(selectAll);
		this.fortuneBeetrootHarvestEntry.setSelected(selectAll);
		this.protectTilledSoilEntry.setSelected(selectAll);
		this.spyglassTreasureVisionEntry.setSelected(selectAll);
		this.uniqueTreasureMapsEntry.setSelected(selectAll);
	}

	private void applyOptions() {
		boolean saved = CapHardConfig.saveGlobalOptions(
			CapHardConfig.consumeContainer(),
			CapHardConfig.showFoodProperties(),
			CapHardConfig.markHiddenInformation(),
			CapHardConfig.preventRottenFleshWolfFeeding(),
			CapHardConfig.preventPrimaryFoodConsumption(),
			CapHardConfig.horseIgnoresLeaves(),
			CapHardConfig.fasterLeafDecay(),
			CapHardConfig.increasedSaplingBeeNestChance(),
			CapHardConfig.beesSurviveStinging(),
			CapHardConfig.showStatusEffectPanel(),
			CapHardConfig.showPotionRecipes(),
			this.fortuneWheatHarvest,
			this.fortuneBeetrootHarvest,
			this.protectTilledSoil,
			this.sortSpecialItemsByDescription,
			this.retainHalfExperienceOnDeath,
			this.infinityWithoutArrow,
			this.showEnchantmentInformation,
			this.spyglassTreasureVision,
			this.uniqueTreasureMaps,
			this.uprightCustomNames,
			this.magneticDropSafeguard,
			this.lodestoneCompassTeleport,
			this.safeChorusTeleport,
			this.clearWeatherLighting,
			this.separateRecipeBookStates
		);
		this.status = Component.translatable(saved ? "caphard.options.status.applied" : "caphard.status.save_failed");
		this.statusColor = saved ? 0xFF9CD67A : 0xFFFF6B6B;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fill(0, 0, this.width, this.height, 0xD0101010);
		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;
		graphics.centeredText(this.font, Component.translatable("caphard.title"), this.width / 2, 14, 0xFFFFFFFF);
		graphics.centeredText(this.font, Component.translatable("caphard.options.subtitle"),
			this.width / 2, 29, 0xFFBDBDBD);
		this.basePanel.render(graphics, this.font, left, 47, contentWidth);
		graphics.text(this.font, this.title, left + 4, 123, 0xFFE0E0E0, true);
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		this.renderInactiveScrollbar(graphics, left, contentWidth);
		this.fortuneWheatHarvestEntry.renderTooltip(graphics, mouseX, mouseY);
		this.fortuneBeetrootHarvestEntry.renderTooltip(graphics, mouseX, mouseY);
		this.protectTilledSoilEntry.renderTooltip(graphics, mouseX, mouseY);
		this.sortSpecialItemsByDescriptionEntry.renderTooltip(graphics, mouseX, mouseY);
		this.retainHalfExperienceOnDeathEntry.renderTooltip(graphics, mouseX, mouseY);
		this.infinityWithoutArrowEntry.renderTooltip(graphics, mouseX, mouseY);
		this.magneticDropSafeguardEntry.renderTooltip(graphics, mouseX, mouseY);
		this.lodestoneCompassTeleportEntry.renderTooltip(graphics, mouseX, mouseY);
		this.safeChorusTeleportEntry.renderTooltip(graphics, mouseX, mouseY);
		this.clearWeatherLightingEntry.renderTooltip(graphics, mouseX, mouseY);
		this.showEnchantmentInformationEntry.renderTooltip(graphics, mouseX, mouseY);
		this.uprightCustomNamesEntry.renderTooltip(graphics, mouseX, mouseY);
		this.separateRecipeBookStatesEntry.renderTooltip(graphics, mouseX, mouseY);
		this.spyglassTreasureVisionEntry.renderTooltip(graphics, mouseX, mouseY);
		this.uniqueTreasureMapsEntry.renderTooltip(graphics, mouseX, mouseY);
		if (!this.status.getString().isEmpty()) {
			graphics.centeredText(this.font, this.status, this.width / 2, this.height - 49, this.statusColor);
		}
	}

	private void renderInactiveScrollbar(GuiGraphicsExtractor graphics, int left, int contentWidth) {
		int scrollbarX = left + contentWidth + SCROLLBAR_GAP;
		int optionsBottom = this.height - 48;
		graphics.fill(scrollbarX, OPTIONS_TOP, scrollbarX + SCROLLBAR_WIDTH, optionsBottom, 0xFF555555);
		graphics.fill(scrollbarX, OPTIONS_TOP, scrollbarX + 1, optionsBottom, 0xFF707070);
		graphics.fill(scrollbarX + SCROLLBAR_WIDTH - 1, OPTIONS_TOP, scrollbarX + SCROLLBAR_WIDTH, optionsBottom,
			0xFF303030);
	}

	@Override
	public void onClose() {
		this.status = Component.empty();
		this.minecraft.gui.setScreen(this.parent);
	}
}
