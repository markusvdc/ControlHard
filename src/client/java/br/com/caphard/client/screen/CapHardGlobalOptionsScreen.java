package br.com.caphard.client.screen;

import br.com.caphard.client.screen.component.ActionButtons;
import br.com.caphard.client.screen.component.CapBasePanel;
import br.com.caphard.client.screen.component.CategoryDivider;
import br.com.caphard.client.screen.component.GlobalOptionEntry;
import br.com.caphard.config.CapHardConfig;
import br.com.caphard.client.screen.component.LocalizedComponentComparator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CapHardGlobalOptionsScreen extends Screen {
	private static final int MAX_CONTENT_WIDTH = 540;
	private static final int SIDE_MARGIN = 16;
	private static final int OPTIONS_TOP = 137;
	private static final int OPTION_HEIGHT = 30;
	private static final int VISIBLE_ROW_COUNT = 13;
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLLBAR_GAP = 6;

	private final List<AbstractWidget> optionRows = new ArrayList<>();
	private int rowHeight;
	private double scrollAmount;
	private boolean draggingScrollbar;
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
	private GlobalOptionEntry reducedRainEffectsEntry;
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
	private boolean reducedRainEffects;
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
		this.rowHeight = Math.max(14, Math.min(OPTION_HEIGHT, (buttonY - 12 - OPTIONS_TOP) / VISIBLE_ROW_COUNT));
		this.optionRows.clear();
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
		this.reducedRainEffects = CapHardConfig.reducedRainEffects();
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
		this.reducedRainEffectsEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.reduced_rain_effects"),
			"caphard.options.reduced_rain_effects",
			this.reducedRainEffects,
			selected -> this.reducedRainEffects = selected
		);
		this.separateRecipeBookStatesEntry = new GlobalOptionEntry(
			left, 0, contentWidth, OPTION_HEIGHT,
			Component.translatable("caphard.options.separate_recipe_book_states"),
			"caphard.options.separate_recipe_book_states",
			this.separateRecipeBookStates,
			selected -> this.separateRecipeBookStates = selected
		);
		addCategory("quality", List.of(
			this.sortSpecialItemsByDescriptionEntry,
			this.showEnchantmentInformationEntry,
			this.uprightCustomNamesEntry,
			this.separateRecipeBookStatesEntry,
			this.magneticDropSafeguardEntry,
			this.clearWeatherLightingEntry,
			this.reducedRainEffectsEntry,
			this.protectTilledSoilEntry,
			this.uniqueTreasureMapsEntry
		), left, contentWidth);
		addCategory("fantasy", List.of(
			this.retainHalfExperienceOnDeathEntry,
			this.infinityWithoutArrowEntry,
			this.lodestoneCompassTeleportEntry,
			this.safeChorusTeleportEntry,
			this.fortuneWheatHarvestEntry,
			this.fortuneBeetrootHarvestEntry,
			this.spyglassTreasureVisionEntry
		), left, contentWidth);
		setScroll(this.scrollAmount);
		ActionButtons actionButtons = new ActionButtons(
			left, buttonY, contentWidth, this::onClose, () -> { },
			this::toggleAllOptions, this::applyOptions, true
		);
		actionButtons.addTo(this::addRenderableWidget);
	}

	private void toggleAllOptions() {
		boolean selectAll = !this.sortSpecialItemsByDescription || !this.showEnchantmentInformation || !this.uprightCustomNames || !this.separateRecipeBookStates || !this.retainHalfExperienceOnDeath || !this.infinityWithoutArrow || !this.magneticDropSafeguard || !this.lodestoneCompassTeleport || !this.safeChorusTeleport || !this.clearWeatherLighting || !this.reducedRainEffects || !this.fortuneWheatHarvest || !this.fortuneBeetrootHarvest || !this.protectTilledSoil || !this.spyglassTreasureVision || !this.uniqueTreasureMaps;
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
		this.reducedRainEffectsEntry.setSelected(selectAll);
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
			this.reducedRainEffects,
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
		graphics.enableScissor(left, OPTIONS_TOP, left + contentWidth, optionsBottom());
		for (AbstractWidget row : this.optionRows) {
			row.extractRenderState(graphics, mouseX, mouseY >= OPTIONS_TOP && mouseY < optionsBottom() ? mouseY : -1, delta);
		}
		graphics.disableScissor();
		renderScrollbar(graphics, left, contentWidth);
		if (mouseY >= OPTIONS_TOP && mouseY < optionsBottom()) {
			for (AbstractWidget row : this.optionRows) {
				if (row instanceof GlobalOptionEntry entry && entry.isMouseOver(mouseX, mouseY)) {
					entry.renderTooltip(graphics, mouseX, mouseY);
				}
			}
		}
		if (!this.status.getString().isEmpty()) {
			graphics.centeredText(this.font, this.status, this.width / 2, this.height - 49, this.statusColor);
		}
	}

	private void addCategory(String category, List<GlobalOptionEntry> entries, int left, int width) {
		this.optionRows.add(new CategoryDivider(left, 0, width, this.rowHeight,
			Component.translatable("caphard.options.category." + category)));
		var sorted = new ArrayList<>(entries);
		var comparator = LocalizedComponentComparator.forCurrentLanguage(this.minecraft);
		sorted.sort((first, second) -> comparator.compare(first.getMessage(), second.getMessage()));
		for (GlobalOptionEntry entry : sorted) {
			entry.setHeight(this.rowHeight);
			entry.setViewport(OPTIONS_TOP, optionsBottom());
			this.optionRows.add(entry);
			this.addWidget(entry);
		}
	}

	private int optionsBottom() {
		return Math.max(OPTIONS_TOP + 1, this.height - 48);
	}

	private int maxScroll() {
		return Math.max(0, this.optionRows.size() * this.rowHeight - (optionsBottom() - OPTIONS_TOP));
	}

	private void setScroll(double amount) {
		this.scrollAmount = Math.clamp(amount, 0.0, (double) maxScroll());
		for (int index = 0; index < this.optionRows.size(); index++) {
			this.optionRows.get(index).setY(OPTIONS_TOP + index * this.rowHeight - (int) this.scrollAmount);
		}
	}

	private int thumbHeight() {
		int height = optionsBottom() - OPTIONS_TOP;
		return Math.min(height, Math.max(24, height * height / Math.max(1, this.optionRows.size() * this.rowHeight)));
	}

	private void scrollFromMouse(double mouseY) {
		int travel = optionsBottom() - OPTIONS_TOP - thumbHeight();
		setScroll(travel <= 0 ? 0 : (mouseY - OPTIONS_TOP - thumbHeight() / 2.0) * maxScroll() / travel);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
		int width = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - width) / 2;
		if (mouseX >= left && mouseX < left + width + SCROLLBAR_GAP + SCROLLBAR_WIDTH
			&& mouseY >= OPTIONS_TOP && mouseY < optionsBottom()) {
			setScroll(this.scrollAmount - vertical * this.rowHeight);
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int width = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int scrollbarX = (this.width - width) / 2 + width + SCROLLBAR_GAP;
		if (event.button() == 0 && event.x() >= scrollbarX && event.x() < scrollbarX + SCROLLBAR_WIDTH
			&& event.y() >= OPTIONS_TOP && event.y() < optionsBottom()) {
			this.draggingScrollbar = maxScroll() > 0;
			scrollFromMouse(event.y());
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double offsetX, double offsetY) {
		if (this.draggingScrollbar && event.button() == 0) {
			scrollFromMouse(event.y());
			return true;
		}
		return super.mouseDragged(event, offsetX, offsetY);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0 && this.draggingScrollbar) {
			this.draggingScrollbar = false;
			return true;
		}
		return super.mouseReleased(event);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		boolean handled = super.keyPressed(event);
		if (this.getFocused() instanceof GlobalOptionEntry entry) {
			if (entry.getY() < OPTIONS_TOP) {
				setScroll(this.scrollAmount + entry.getY() - OPTIONS_TOP);
			} else if (entry.getY() + entry.getHeight() > optionsBottom()) {
				setScroll(this.scrollAmount + entry.getY() + entry.getHeight() - optionsBottom());
			}
		}
		return handled;
	}

	private void renderScrollbar(GuiGraphicsExtractor graphics, int left, int contentWidth) {
		int x = left + contentWidth + SCROLLBAR_GAP;
		int travel = optionsBottom() - OPTIONS_TOP - thumbHeight();
		int y = OPTIONS_TOP + (maxScroll() == 0 ? 0 : (int) (travel * this.scrollAmount / maxScroll()));
		graphics.fill(x, OPTIONS_TOP, x + SCROLLBAR_WIDTH, optionsBottom(), 0xFF080808);
		graphics.fill(x, y, x + SCROLLBAR_WIDTH, y + thumbHeight(), maxScroll() == 0 ? 0xFF555555 : 0xFFC0C0C0);
		graphics.fill(x, y, x + 1, y + thumbHeight(), maxScroll() == 0 ? 0xFF707070 : 0xFFFFFFFF);
		graphics.fill(x + SCROLLBAR_WIDTH - 1, y, x + SCROLLBAR_WIDTH, y + thumbHeight(),
			maxScroll() == 0 ? 0xFF303030 : 0xFF707070);
	}
	@Override
	public void onClose() {
		this.status = Component.empty();
		this.minecraft.gui.setScreen(this.parent);
	}
}
