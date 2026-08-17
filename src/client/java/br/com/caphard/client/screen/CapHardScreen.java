package br.com.caphard.client.screen;

import br.com.caphard.client.screen.component.ActionButtons;
import br.com.caphard.client.screen.component.CapBasePanel;
import br.com.caphard.client.screen.component.RuleSelectionList;
import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CapHardScreen extends Screen {
	private static final int MAX_CONTENT_WIDTH = 540;
	private static final int SIDE_MARGIN = 16;
	private static final int SCROLLBAR_OVERFLOW = 12;
	private static final int LIST_TOP = 137;
	private static final int ROW_HEIGHT = 30;

	private final Screen parent;
	private final CapBasePanel basePanel = new CapBasePanel();
	private RuleSelectionList ruleList;
	private Component status = Component.empty();
	private int statusColor = 0xFF9CD67A;

	public CapHardScreen(Screen parent) {
		super(Component.translatable("caphard.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;
		int buttonY = this.height - 36;
		int listBottom = Math.max(LIST_TOP + 56, buttonY - 12);

		this.ruleList = new RuleSelectionList(
			this.minecraft,
			contentWidth + SCROLLBAR_OVERFLOW,
			listBottom - LIST_TOP,
			LIST_TOP,
			ROW_HEIGHT
		);
		this.ruleList.setX(left);
		this.addRenderableWidget(this.ruleList);

		ActionButtons actionButtons = new ActionButtons(
			left,
			buttonY,
			contentWidth,
			this::onClose,
			null,
			this::toggleAllRules,
			this::applySelection,
			false
		);
		actionButtons.addTo(this::addRenderableWidget);
	}

	private void clearStatus() {
		this.status = Component.empty();
	}

	private void applySelection() {
		boolean saved = CapHardConfig.saveRules(this.ruleList.selectedRules());
		this.status = Component.translatable(saved ? "caphard.status.applied" : "caphard.status.save_failed");
		this.statusColor = saved ? 0xFF9CD67A : 0xFFFF6B6B;
	}

	private void toggleAllRules() {
		this.ruleList.setAllSelected(!this.ruleList.areAllSelected());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fill(0, 0, this.width, this.height, 0xD0101010);

		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;

		graphics.centeredText(this.font, this.title, this.width / 2, 14, 0xFFFFFFFF);
		graphics.centeredText(
			this.font,
			Component.translatable("caphard.subtitle"),
			this.width / 2,
			29,
			0xFFBDBDBD
		);

		this.basePanel.render(graphics, this.font, left, 47, contentWidth);
		graphics.text(this.font, Component.translatable("caphard.list.title"), left + 4, 123, 0xFFE0E0E0, true);

		super.extractRenderState(graphics, mouseX, mouseY, delta);
		this.ruleList.renderTooltip(graphics, mouseX, mouseY);

		if (!this.status.getString().isEmpty()) {
			graphics.centeredText(this.font, this.status, this.width / 2, this.height - 49, this.statusColor);
		}
	}

	@Override
	public void onClose() {
		this.clearStatus();
		this.minecraft.gui.setScreen(this.parent);
	}
}
