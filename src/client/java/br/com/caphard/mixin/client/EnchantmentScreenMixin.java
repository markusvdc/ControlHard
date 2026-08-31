package br.com.caphard.mixin.client;

import br.com.caphard.client.EnchantmentInformationTooltip;
import br.com.caphard.config.CapHardConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {
	private static final int IMAGE_WIDTH = 176;
	private static final int IMAGE_HEIGHT = 166;
	@Unique
	private Holder<Enchantment> caphard$hoveredEnchantment;

	@Inject(method = "extractRenderState", at = @At("HEAD"))
	private void caphard$findHoveredEnchantment(
		GuiGraphicsExtractor graphics,
		int mouseX,
		int mouseY,
		float delta,
		CallbackInfo callbackInfo
	) {
		this.caphard$hoveredEnchantment = null;
		if (!CapHardConfig.showEnchantmentInformation()) {
			return;
		}

		EnchantmentScreen screen = (EnchantmentScreen) (Object) this;
		EnchantmentMenu menu = (EnchantmentMenu) ((AbstractContainerScreenAccessor) screen).caphard$getMenu();
		int left = (screen.width - IMAGE_WIDTH) / 2;
		int top = (screen.height - IMAGE_HEIGHT) / 2;
		int option = -1;
		for (int index = 0; index < 3; index++) {
			int optionTop = top + 14 + index * 19;
			if (mouseX >= left + 59 && mouseX < left + 169
				&& mouseY >= optionTop - 1 && mouseY < optionTop + 18) {
				option = index;
				break;
			}
		}
		if (option < 0) {
			return;
		}

		int enchantmentId = menu.enchantClue[option];
		Minecraft minecraft = Minecraft.getInstance();
		if (enchantmentId < 0 || minecraft.level == null || minecraft.player == null) {
			return;
		}
		if (!minecraft.player.hasInfiniteMaterials() && minecraft.player.experienceLevel < menu.costs[option]) {
			return;
		}
		this.caphard$hoveredEnchantment = minecraft.level.registryAccess()
			.lookupOrThrow(Registries.ENCHANTMENT)
			.get(enchantmentId)
			.orElse(null);
	}

	@ModifyArg(
		method = "extractRenderState",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;setComponentTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V"
		),
		index = 1
	)
	private List<Component> caphard$appendEnchantmentInformation(List<Component> original) {
		Holder<Enchantment> enchantment = this.caphard$hoveredEnchantment;
		if (enchantment == null || !EnchantmentInformationTooltip.hasInformation(enchantment)) {
			return original;
		}

		List<Component> expanded = new ArrayList<>(original.size() + 3);
		expanded.addAll(original);
		expanded.add(CommonComponents.EMPTY);
		EnchantmentInformationTooltip.append(enchantment, expanded);
		return expanded;
	}
}
