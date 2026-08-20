package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class FortuneHarvestBonus {
	private static final float EXTRA_CROP_CHANCE = 0.30F;
	private static final int REQUIRED_FORTUNE_LEVEL = 3;
	private static final Map<Player, PendingBonus> PENDING_BONUSES = new WeakHashMap<>();

	private FortuneHarvestBonus() {
	}

	public static void register() {
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
			PENDING_BONUSES.remove(player);
			if (!(level instanceof ServerLevel serverLevel) || player.isCreative()) {
				return true;
			}

			Item bonusItem = eligibleBonusItem(state);
			if (bonusItem != null && hasFortuneThree(serverLevel, player.getMainHandItem())
				&& serverLevel.getRandom().nextFloat() < EXTRA_CROP_CHANCE) {
				PENDING_BONUSES.put(player, new PendingBonus(pos.immutable(), bonusItem));
			}
			return true;
		});

		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			PendingBonus pending = PENDING_BONUSES.remove(player);
			if (pending != null && pending.pos().equals(pos)) {
				Block.popResource(level, pos, new ItemStack(pending.item()));
			}
		});

		PlayerBlockBreakEvents.CANCELED.register((level, player, pos, state, blockEntity) ->
			PENDING_BONUSES.remove(player));
	}

	private static Item eligibleBonusItem(BlockState state) {
		if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state)) {
			return null;
		}
		if (state.is(Blocks.WHEAT) && CapHardConfig.fortuneWheatHarvest()) {
			return Items.WHEAT;
		}
		if (state.is(Blocks.BEETROOTS) && CapHardConfig.fortuneBeetrootHarvest()) {
			return Items.BEETROOT;
		}
		return null;
	}

	private static boolean hasFortuneThree(ServerLevel level, ItemStack tool) {
		var fortune = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
		return EnchantmentHelper.getItemEnchantmentLevel(fortune, tool) >= REQUIRED_FORTUNE_LEVEL;
	}

	private record PendingBonus(BlockPos pos, Item item) {
	}
}
