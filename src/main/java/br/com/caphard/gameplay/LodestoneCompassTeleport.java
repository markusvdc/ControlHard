package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;

import java.util.Set;

public final class LodestoneCompassTeleport {
	private static final int EMERALD_COST = 15;

	private LodestoneCompassTeleport() {
	}

	public static void register() {
		UseItemCallback.EVENT.register(LodestoneCompassTeleport::tryTeleport);
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> tryTeleport(player, level, hand));
	}

	private static InteractionResult tryTeleport(Player player, Level level, InteractionHand hand) {
		ItemStack compass = player.getItemInHand(hand);
		LodestoneTracker tracker = compass.get(DataComponents.LODESTONE_TRACKER);
		if (!CapHardConfig.lodestoneCompassTeleport() || tracker == null || tracker.target().isEmpty()) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		}

		Inventory inventory = player.getInventory();
		if (countEmeralds(inventory) < EMERALD_COST) {
			return InteractionResult.PASS;
		}

		consumeEmeralds(inventory);
		GlobalPos target = tracker.target().orElseThrow();
		ServerPlayer serverPlayer = (ServerPlayer) player;
		ServerLevel targetLevel = ((ServerLevel) level).getServer().getLevel(target.dimension());
		serverPlayer.teleportTo(
			targetLevel,
			target.pos().getX() + 0.5,
			target.pos().getY() + 1.0,
			target.pos().getZ() + 0.5,
			Set.of(),
			serverPlayer.getYRot(),
			serverPlayer.getXRot(),
			false
		);
		return InteractionResult.SUCCESS;
	}

	private static int countEmeralds(Inventory inventory) {
		int count = 0;
		for (ItemStack stack : inventory.getNonEquipmentItems()) {
			if (stack.is(Items.EMERALD)) {
				count += stack.getCount();
			}
		}
		return count;
	}

	private static void consumeEmeralds(Inventory inventory) {
		int remaining = EMERALD_COST;
		for (ItemStack stack : inventory.getNonEquipmentItems()) {
			if (!stack.is(Items.EMERALD)) {
				continue;
			}
			int consumed = Math.min(remaining, stack.getCount());
			stack.shrink(consumed);
			remaining -= consumed;
			if (remaining == 0) {
				break;
			}
		}
		inventory.setChanged();
	}
}
