package br.com.caphard.client;

import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public final class StatusEffectHudRenderer {
	private static final Identifier EFFECT_BACKGROUND = Identifier.withDefaultNamespace("container/inventory/effect_background");
	private static final Identifier AMBIENT_EFFECT_BACKGROUND = Identifier.withDefaultNamespace("container/inventory/effect_background_ambient");
	private static final int PANEL_WIDTH = 120;
	private static final int PANEL_HEIGHT = 32;
	private static final int RIGHT_MARGIN = 4;
	private static final int TOP_MARGIN = 4;
	private static final Set<String> ABBREVIATED_EFFECTS = Set.of(
		"instant_damage",
		"instant_health",
		"hero_of_the_village",
		"raid_omen",
		"trial_omen",
		"conduit_power",
		"water_breathing",
		"breath_of_the_nautilus",
		"fire_resistance"
	);

	private StatusEffectHudRenderer() {
	}

	public static void extract(GuiGraphicsExtractor graphics) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.level == null) {
			return;
		}

		Collection<MobEffectInstance> activeEffects = minecraft.player.getActiveEffects();
		if (activeEffects.isEmpty()) {
			return;
		}

		int yStep = activeEffects.size() > 5 ? 132 / (activeEffects.size() - 1) : 33;
		int x = graphics.guiWidth() - PANEL_WIDTH - RIGHT_MARGIN;
		int y = TOP_MARGIN;
		Font font = minecraft.font;
		int referenceNameWidth = font.width(Component.translatable("effect.minecraft.night_vision"));

		for (MobEffectInstance effect : Ordering.natural().sortedCopy(activeEffects)) {
			Identifier background = effect.isAmbient() ? AMBIENT_EFFECT_BACKGROUND : EFFECT_BACKGROUND;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, x, y, PANEL_WIDTH, PANEL_HEIGHT);
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Hud.getMobEffectSprite(effect.getEffect()), x + 7, y + 7, 18, 18);

			Component name = getEffectName(effect, font, referenceNameWidth);
			Component duration = MobEffectUtil.formatDuration(effect, 1.0F, minecraft.level.tickRateManager().tickrate());
			graphics.text(font, name, x + 32, y + 7, -1);
			graphics.text(font, duration, x + 32, y + 18, -8355712);
			y += yStep;
		}
	}

	private static Component getEffectName(MobEffectInstance effect, Font font, int referenceWidth) {
		MutableComponent name = effect.getEffect().value().getDisplayName().copy();
		String effectId = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value()).getPath();
		if (font.width(name) > referenceWidth && ABBREVIATED_EFFECTS.contains(effectId)) {
			name = Component.translatable("caphard.effect_abbreviation." + effectId);
		}
		if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9) {
			name.append(CommonComponents.SPACE)
				.append(Component.translatable("enchantment.level." + (effect.getAmplifier() + 1)));
		}
		return name;
	}
}
