package br.com.caphard.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "org.anti_ad.mc.ipnext.item.ItemType", remap = false)
public abstract class InventoryProfilesItemTypeMixin {
	private static final boolean CAPHARD$APPLY_IPN_COMPATIBILITY =
		FabricLoader.getInstance().isModLoaded("inventoryprofilesnext");

	@Shadow(remap = false)
	public abstract Item getItem();

	@Shadow(remap = false)
	public abstract DataComponentPatch getChanges();

	@Shadow(remap = false)
	public abstract boolean getIgnoreDurability();

	@Inject(method = "hashCode", at = @At("HEAD"), cancellable = true, remap = false)
	private void caphard$useStableComponentHash(CallbackInfoReturnable<Integer> callback) {
		if (!CAPHARD$APPLY_IPN_COMPATIBILITY) {
			return;
		}

		DataComponentPatch comparableChanges = this.getChanges();
		if (this.getIgnoreDurability()) {
			comparableChanges = comparableChanges.forget(type -> type == DataComponents.DAMAGE);
		}

		callback.setReturnValue(31 * this.getItem().hashCode() + comparableChanges.hashCode());
	}
}
