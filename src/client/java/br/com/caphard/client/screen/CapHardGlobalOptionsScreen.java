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
	private static final int VISIBLE_ROW_COUNT = 13;
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLLBAR_GAP = 6;

	private final Screen parent;
	private final CapBasePanel basePanel = new CapBasePanel();
	private GlobalOptionEntry fortuneWheatHarvestEntry;
	private GlobalOptionEntry fortuneBeetrootHarvestEntry;
	private boolean fortuneWheatHarvest;
	private boolean fortuneBeetrootHarvest;
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
		int row = 0;
		this.addRenderableWidget(new CategoryDivider(left, OPTIONS_TOP + rowHeight * row++, contentWidth, rowHeight,
			Component.translatable("caphard.options.category.quality")));
		this.fortuneWheatHarvestEntry.setHeight(rowHeight);
		this.fortuneWheatHarvestEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.fortuneWheatHarvestEntry);
		row++;
		this.fortuneBeetrootHarvestEntry.setHeight(rowHeight);
		this.fortuneBeetrootHarvestEntry.setY(OPTIONS_TOP + rowHeight * row);
		this.addRenderableWidget(this.fortuneBeetrootHarvestEntry);

		ActionButtons actionButtons = new ActionButtons(
			left, buttonY, contentWidth, this::onClose, () -> { },
			this::toggleAllOptions, this::applyOptions, true
		);
		actionButtons.addTo(this::addRenderableWidget);
	}

	private void toggleAllOptions() {
		boolean selectAll = !this.fortuneWheatHarvest || !this.fortuneBeetrootHarvest;
		this.fortuneWheatHarvestEntry.setSelected(selectAll);
		this.fortuneBeetrootHarvestEntry.setSelected(selectAll);
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
			this.fortuneBeetrootHarvest
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
