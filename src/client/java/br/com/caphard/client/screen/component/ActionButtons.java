package br.com.caphard.client.screen.component;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public final class ActionButtons {
	private static final int GAP = 8;

	private final HoverOnlyButton backButton;
	private final HoverOnlyButton optionsButton;
	private final HoverOnlyButton applyButton;
	private final HoverOnlyButton selectAllButton;

	public ActionButtons(
		int x,
		int y,
		int width,
		Runnable onBack,
		Runnable onOptions,
		Runnable onToggleAll,
		Runnable onApply,
		boolean silentOptions
	) {
		boolean hasOptions = onOptions != null;
		int buttonCount = hasOptions ? 4 : 3;
		int buttonWidth = (width - GAP * (buttonCount - 1)) / buttonCount;
		int applyWidth = width - buttonWidth * (buttonCount - 1) - GAP * (buttonCount - 1);

		this.backButton = new HoverOnlyButton(
			x, y, buttonWidth, 20, Component.translatable("caphard.action.back"), button -> onBack.run()
		);
		this.optionsButton = hasOptions ? new HoverOnlyButton(
			x + buttonWidth + GAP, y, buttonWidth, 20,
			Component.translatable("caphard.action.options"), button -> onOptions.run(), silentOptions
		) : null;
		int toggleIndex = hasOptions ? 2 : 1;
		this.selectAllButton = new HoverOnlyButton(
			x + buttonWidth * toggleIndex + GAP * toggleIndex,
			y,
			buttonWidth,
			20,
			accentedLabel("caphard.action.toggle_all", ChatFormatting.GOLD),
			button -> onToggleAll.run()
		);
		this.selectAllButton.setTooltip(Tooltip.create(Component.translatable("caphard.action.toggle_all.tooltip")));
		this.applyButton = new HoverOnlyButton(
			x + buttonWidth * (buttonCount - 1) + GAP * (buttonCount - 1),
			y,
			applyWidth,
			20,
			accentedLabel("caphard.action.apply", ChatFormatting.GREEN),
			button -> onApply.run()
		);
	}

	public void addTo(Consumer<HoverOnlyButton> addWidget) {
		addWidget.accept(this.backButton);
		if (this.optionsButton != null) {
			addWidget.accept(this.optionsButton);
		}
		addWidget.accept(this.selectAllButton);
		addWidget.accept(this.applyButton);
	}

	private static Component accentedLabel(String translationKey, ChatFormatting color) {
		return Component.empty()
			.append(Component.literal("■ ").withStyle(color))
			.append(Component.translatable(translationKey));
	}
}
