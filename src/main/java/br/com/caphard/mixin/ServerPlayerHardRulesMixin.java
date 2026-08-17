package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerHardRulesMixin {
	private static final List<EquipmentSlot> CAPHARD$DEATH_EQUIPMENT = List.of(
		EquipmentSlot.OFFHAND,
		EquipmentSlot.FEET,
		EquipmentSlot.LEGS,
		EquipmentSlot.CHEST,
		EquipmentSlot.HEAD
	);

	@Inject(method = "die", at = @At("HEAD"))
	private void caphard$destroyRandomItemsOnHardDeath(DamageSource source, CallbackInfo callback) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		if (!HardRules.isActive(player.level(), HardRule.DEATH_ITEM_LOSS)) {
			return;
		}

		List<EquipmentSlot> occupiedEquipment = new ArrayList<>();
		for (EquipmentSlot slot : CAPHARD$DEATH_EQUIPMENT) {
			if (!player.getItemBySlot(slot).isEmpty()) {
				occupiedEquipment.add(slot);
			}
		}
		if (!occupiedEquipment.isEmpty()) {
			EquipmentSlot selected = occupiedEquipment.get(player.getRandom().nextInt(occupiedEquipment.size()));
			player.setItemSlot(selected, ItemStack.EMPTY);
		}

		Inventory inventory = player.getInventory();
		List<Integer> occupiedHotbar = new ArrayList<>();
		for (int slot = 0; slot < Inventory.getSelectionSize(); slot++) {
			if (!inventory.getItem(slot).isEmpty()) {
				occupiedHotbar.add(slot);
			}
		}
		if (!occupiedHotbar.isEmpty()) {
			int selected = occupiedHotbar.get(player.getRandom().nextInt(occupiedHotbar.size()));
			inventory.removeItemNoUpdate(selected);
		}
	}
}
