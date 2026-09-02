package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {
	@Shadow
	@Final
	protected RecipeBookMenu menu;

	@Redirect(
		method = "isVisibleAccordingToBookData",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/ClientRecipeBook;isOpen(Lnet/minecraft/world/inventory/RecipeBookType;)Z"
		)
	)
	private boolean caphard$readIndependentVisibility(ClientRecipeBook recipeBook, RecipeBookType type) {
		if (!CapHardConfig.separateRecipeBookStates()) {
			return recipeBook.isOpen(type);
		}
		if (this.menu instanceof InventoryMenu) {
			return CapHardConfig.inventoryRecipeBookOpen();
		}
		if (this.menu instanceof CraftingMenu) {
			return CapHardConfig.craftingTableRecipeBookOpen();
		}
		return recipeBook.isOpen(type);
	}

	@Inject(method = "setVisible", at = @At("TAIL"))
	private void caphard$saveIndependentVisibility(boolean visible, CallbackInfo callbackInfo) {
		if (!CapHardConfig.separateRecipeBookStates()) {
			return;
		}
		if (this.menu instanceof InventoryMenu && CapHardConfig.inventoryRecipeBookOpen() != visible) {
			CapHardConfig.saveRecipeBookState(true, visible);
		} else if (this.menu instanceof CraftingMenu && CapHardConfig.craftingTableRecipeBookOpen() != visible) {
			CapHardConfig.saveRecipeBookState(false, visible);
		}
	}
}
