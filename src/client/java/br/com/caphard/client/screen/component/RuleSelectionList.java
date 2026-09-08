package br.com.caphard.client.screen.component;

import br.com.caphard.gameplay.HardRule;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;

public final class RuleSelectionList extends AbstractWidget {
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLLBAR_GAP = 6;
	private static final int VANILLA_TOOLTIP_MAX_WIDTH = 170;
	private static final int TOOLTIP_MAX_WIDTH = VANILLA_TOOLTIP_MAX_WIDTH * 5 / 2;

	private final List<RuleEntry> entries;
	private final int rowHeight;
	private boolean draggingScrollbar;
	private double scrollAmount;

	public RuleSelectionList(Minecraft minecraft, int width, int height, int y, int rowHeight) {
		super(0, y, width, height, Component.translatable("caphard.list.title"));
		this.rowHeight = rowHeight;
		List<RuleEntry> entries = new ArrayList<>(List.of(
			RuleEntry.category(minecraft, "caphard.category.rules"),
			new RuleEntry(minecraft, HardRule.UNLOCK_DIFFICULTY, Items.IRON_DOOR),
			new RuleEntry(minecraft, HardRule.PLAYER_DAMAGE, Items.IRON_SWORD),
			new RuleEntry(minecraft, HardRule.CREEPER_DAMAGE, Items.CREEPER_HEAD),
			new RuleEntry(minecraft, HardRule.MOUNT_DAMAGE, Items.SADDLE),
			new RuleEntry(minecraft, HardRule.SPEAR_DAMAGE, Items.IRON_SPEAR),
			new RuleEntry(minecraft, HardRule.ZOMBIE_IRON_EQUIPMENT, Items.IRON_HELMET),
			new RuleEntry(minecraft, HardRule.SPIDER_EFFECT, Items.SPIDER_EYE),
			new RuleEntry(minecraft, HardRule.PHANTOM_SPEED, Items.PHANTOM_MEMBRANE),
			new RuleEntry(minecraft, HardRule.TWELVE_WAVE_RAIDS, Items.OMINOUS_BOTTLE),
			new RuleEntry(minecraft, HardRule.WITCH_WEIGHT, Items.POTION),
			new RuleEntry(minecraft, HardRule.WITCH_SLOWNESS_DURATION, Items.SPLASH_POTION),
			new RuleEntry(minecraft, HardRule.ENDERMAN_WEIGHT, Items.ENDER_PEARL),
			new RuleEntry(minecraft, HardRule.MOUNT_TARGETING, Items.CARROT_ON_A_STICK),
			new RuleEntry(minecraft, HardRule.HORSE_IMMUNITY, Items.GOLDEN_HORSE_ARMOR),
			new RuleEntry(minecraft, HardRule.CAPPED_EXPERIENCE_COST, Items.EXPERIENCE_BOTTLE),
			new RuleEntry(minecraft, HardRule.DEATH_ITEM_LOSS, Items.TOTEM_OF_UNDYING)
		));
		sortWithinCategories(entries, LocalizedComponentComparator.forCurrentLanguage(minecraft));
		this.entries = List.copyOf(entries);
	}

	private static void sortWithinCategories(List<RuleEntry> entries, Comparator<Component> comparator) {
		int sectionStart = 0;
		while (sectionStart < entries.size()) {
			if (entries.get(sectionStart).isCategory()) {
				sectionStart++;
			}
			int sectionEnd = sectionStart;
			while (sectionEnd < entries.size() && !entries.get(sectionEnd).isCategory()) {
				sectionEnd++;
			}
			entries.subList(sectionStart, sectionEnd).sort(
				(first, second) -> comparator.compare(first.name(), second.name())
			);
			sectionStart = sectionEnd;
		}
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int x = getX();
		int y = getY();
		int contentWidth = this.width - SCROLLBAR_WIDTH - SCROLLBAR_GAP;
		boolean needsScrollbar = getMaxScroll() > 0;

		graphics.enableScissor(x, y, x + this.width, y + this.height);
		graphics.fill(x, y, x + this.width, y + this.height, 0xB8101010);

		for (int index = 0; index < this.entries.size(); index++) {
			int rowY = getRowY(index);
			if (!isRowVisible(rowY)) {
				continue;
			}
			RuleEntry entry = this.entries.get(index);
			boolean hovered = !entry.isCategory()
				&& mouseX >= x
				&& mouseX < x + contentWidth
				&& mouseY >= rowY
				&& mouseY < rowY + this.rowHeight;
			entry.renderBackground(graphics, x, rowY, contentWidth, this.rowHeight, hovered);
		}

		drawScrollbarTrack(graphics);
		graphics.disableScissor();

		graphics.nextStratum();
		graphics.enableScissor(x, y, x + this.width, y + this.height);
		for (int index = 0; index < this.entries.size(); index++) {
			int rowY = getRowY(index);
			if (isRowVisible(rowY)) {
				this.entries.get(index).renderContent(graphics, x, rowY, this.rowHeight);
			}
		}
		if (needsScrollbar) {
			drawScrollbarThumb(graphics);
		} else {
			drawInactiveScrollbarThumb(graphics);
		}
		graphics.disableScissor();
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() != 0 || !isMouseOver(event.x(), event.y())) {
			return false;
		}

		int contentWidth = this.width - SCROLLBAR_WIDTH - SCROLLBAR_GAP;
		if (event.x() >= getX() + contentWidth) {
			this.draggingScrollbar = getMaxScroll() > 0;
			setScrollFromMouse(event.y());
			return true;
		}

		int index = (int) ((event.y() - getY() + this.scrollAmount) / this.rowHeight);
		if (index >= 0 && index < this.entries.size()) {
			RuleEntry entry = this.entries.get(index);
			if (!entry.isCategory()) {
				entry.toggle();
				playDownSound(Minecraft.getInstance().getSoundManager());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double offsetX, double offsetY) {
		if (!this.draggingScrollbar || event.button() != 0) {
			return false;
		}
		setScrollFromMouse(event.y());
		return true;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0 && this.draggingScrollbar) {
			this.draggingScrollbar = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (!isMouseOver(mouseX, mouseY)) {
			return false;
		}
		this.scrollAmount = Mth.clamp(
			this.scrollAmount - verticalAmount * this.rowHeight,
			0.0,
			getMaxScroll()
		);
		return true;
	}

	public void setAllSelected(boolean selected) {
		this.entries.forEach(entry -> entry.setSelected(selected));
	}

	public boolean areAllSelected() {
		return this.entries.stream()
			.filter(entry -> !entry.isCategory())
			.allMatch(RuleEntry::isSelected);
	}

	public Set<HardRule> selectedRules() {
		return this.entries.stream()
			.filter(entry -> !entry.isCategory() && entry.isSelected())
			.map(RuleEntry::rule)
			.collect(Collectors.toUnmodifiableSet());
	}

	public void renderTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		int contentWidth = this.width - SCROLLBAR_WIDTH - SCROLLBAR_GAP;
		if (mouseX < getX() || mouseX >= getX() + contentWidth || mouseY < getY() || mouseY >= getY() + this.height) {
			return;
		}
		int index = (int) ((mouseY - getY() + this.scrollAmount) / this.rowHeight);
		if (index < 0 || index >= this.entries.size()) {
			return;
		}
		RuleEntry entry = this.entries.get(index);
		if (entry.isCategory()) {
			return;
		}
		List<FormattedCharSequence> wrappedLines = entry.tooltip().split(
			Minecraft.getInstance().font,
			TOOLTIP_MAX_WIDTH
		);
		List<ClientTooltipComponent> lines = java.util.stream.IntStream.range(0, wrappedLines.size())
			.mapToObj(lineIndex -> new RuleTooltipLine(
				wrappedLines.get(lineIndex),
				lineIndex == 0,
				lineIndex == wrappedLines.size() - 1
			))
			.map(ClientTooltipComponent.class::cast)
			.toList();
		graphics.tooltip(
			Minecraft.getInstance().font,
			lines,
			mouseX,
			mouseY,
			DefaultTooltipPositioner.INSTANCE,
			null
		);
	}

	private int getRowY(int index) {
		return getY() + index * this.rowHeight - (int) this.scrollAmount;
	}

	private boolean isRowVisible(int rowY) {
		return rowY + this.rowHeight > getY() && rowY < getY() + this.height;
	}

	private int getMaxScroll() {
		return Math.max(0, this.entries.size() * this.rowHeight - this.height);
	}

	private void setScrollFromMouse(double mouseY) {
		int travel = this.height - getThumbHeight();
		double relative = Mth.clamp((mouseY - getY() - getThumbHeight() / 2.0) / travel, 0.0, 1.0);
		this.scrollAmount = relative * getMaxScroll();
	}

	private int getThumbHeight() {
		return Math.max(24, this.height * this.height / (this.entries.size() * this.rowHeight));
	}

	private int getThumbY() {
		int travel = this.height - getThumbHeight();
		return getY() + (getMaxScroll() == 0 ? 0 : (int) (travel * this.scrollAmount / getMaxScroll()));
	}

	private void drawScrollbarTrack(GuiGraphicsExtractor graphics) {
		int scrollbarX = getX() + this.width - SCROLLBAR_WIDTH;
		graphics.fill(scrollbarX, getY(), scrollbarX + SCROLLBAR_WIDTH, getY() + this.height, 0xFF080808);
	}

	private void drawScrollbarThumb(GuiGraphicsExtractor graphics) {
		int scrollbarX = getX() + this.width - SCROLLBAR_WIDTH;
		int thumbY = getThumbY();
		graphics.fill(scrollbarX, thumbY, scrollbarX + SCROLLBAR_WIDTH, thumbY + getThumbHeight(), 0xFFC0C0C0);
		graphics.fill(scrollbarX, thumbY, scrollbarX + 1, thumbY + getThumbHeight(), 0xFFFFFFFF);
		graphics.fill(scrollbarX + SCROLLBAR_WIDTH - 1, thumbY, scrollbarX + SCROLLBAR_WIDTH, thumbY + getThumbHeight(), 0xFF707070);
	}

	private void drawInactiveScrollbarThumb(GuiGraphicsExtractor graphics) {
		int scrollbarX = getX() + this.width - SCROLLBAR_WIDTH;
		graphics.fill(scrollbarX, getY(), scrollbarX + SCROLLBAR_WIDTH, getY() + this.height, 0xFF555555);
		graphics.fill(scrollbarX, getY(), scrollbarX + 1, getY() + this.height, 0xFF707070);
		graphics.fill(scrollbarX + SCROLLBAR_WIDTH - 1, getY(), scrollbarX + SCROLLBAR_WIDTH, getY() + this.height,
			0xFF303030);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		defaultButtonNarrationText(output);
	}
}
