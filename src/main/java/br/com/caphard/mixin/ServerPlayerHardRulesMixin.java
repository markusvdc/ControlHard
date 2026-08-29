package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.gameplay.HardRules;
import br.com.caphard.gameplay.HardRule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.gamerules.GameRules;
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

	@Inject(method = "restoreFrom", at = @At("TAIL"))
	private void caphard$retainHalfExperienceOnDeath(
		ServerPlayer previousPlayer,
		boolean keepEverything,
		CallbackInfo callback
	) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		if (keepEverything
			|| !CapHardConfig.retainHalfExperienceOnDeath()
			|| player.level().getGameRules().get(GameRules.KEEP_INVENTORY)
			|| previousPlayer.isSpectator()) {
			return;
		}

		int retainedExperience = caphard$currentExperiencePoints(previousPlayer) / 2;
		int score = player.getScore();
		player.experienceLevel = 0;
		player.experienceProgress = 0.0F;
		player.totalExperience = 0;
		player.giveExperiencePoints(retainedExperience);
		player.setScore(score);
	}

	private static int caphard$currentExperiencePoints(ServerPlayer player) {
		int level = player.experienceLevel;
		long completedLevels;
		if (level <= 16) {
			completedLevels = (long)level * level + 6L * level;
		} else if (level <= 31) {
			completedLevels = (5L * level * level - 81L * level + 720L) / 2L;
		} else {
			completedLevels = (9L * level * level - 325L * level + 4440L) / 2L;
		}
		long progress = (long)Math.floor(player.experienceProgress * player.getXpNeededForNextLevel());
		return (int)Math.min(Integer.MAX_VALUE, completedLevels + progress);
	}

	@Inject(method = "die", at = @At("HEAD"))
	private void caphard$destroyRandomItemsOnHardDeath(DamageSource source, CallbackInfo callback) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		if (!HardRules.isActive(player.level(), HardRule.DEATH_ITEM_LOSS)) {
			return;
		}

		List<EquipmentSlot> occupiedEquipment = new ArrayList<>();
		for (EquipmentSlot slot : CAPHARD$DEATH_EQUIPMENT) {
			if (caphard$canDestroyOnDeath(player.getItemBySlot(slot))) {
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
			if (caphard$canDestroyOnDeath(inventory.getItem(slot))) {
				occupiedHotbar.add(slot);
			}
		}
		if (!occupiedHotbar.isEmpty()) {
			int selected = occupiedHotbar.get(player.getRandom().nextInt(occupiedHotbar.size()));
			inventory.removeItemNoUpdate(selected);
		}
	}

	private static boolean caphard$canDestroyOnDeath(ItemStack stack) {
		return !stack.isEmpty()
			&& !(stack.getItem() instanceof BlockItem blockItem
				&& blockItem.getBlock() instanceof ShulkerBoxBlock);
	}
}
