package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.nyon.magnetic.DropEvent", remap = false)
public abstract class MagneticDropSafeguardMixin {
	private static final Logger CAPHARD$LOGGER = LoggerFactory.getLogger("ControlHard/MagneticDropSafeguard");

	@Redirect(
		method = "canAddItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Inventory;getFreeSlot()I",
			remap = true
		),
		remap = false
	)
	private int caphard$preserveDropWhenFreeSlotCheckFails(Inventory inventory) {
		if (!CapHardConfig.magneticDropSafeguard()) {
			return inventory.getFreeSlot();
		}

		try {
			return inventory.getFreeSlot();
		} catch (RuntimeException exception) {
			caphard$reportCompatibilityFailure(exception);
			return -1;
		}
	}

	@Redirect(
		method = "invoke$lambda$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;addItem(Lnet/minecraft/world/item/ItemStack;)Z",
			remap = true
		),
		remap = false
	)
	private static boolean caphard$preserveDropWhenInventoryInsertionFails(
		ServerPlayer player,
		ItemStack stack
	) {
		if (!CapHardConfig.magneticDropSafeguard()) {
			return player.addItem(stack);
		}

		try {
			return player.addItem(stack);
		} catch (RuntimeException exception) {
			caphard$reportCompatibilityFailure(exception);
			return false;
		}
	}

	private static void caphard$reportCompatibilityFailure(RuntimeException exception) {
		CAPHARD$LOGGER.warn(
			"Magnetic inventory collection failed; preserving the affected drop in the world.",
			exception
		);
	}
}
