package br.com.caphard.mixin;

import br.com.caphard.gameplay.FastLeafDecay;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "playerDestroy", at = @At("TAIL"))
	private void caphard$estimateCutTreeCanopy(
		Level level,
		Player player,
		BlockPos pos,
		BlockState state,
		@Nullable BlockEntity blockEntity,
		ItemStack destroyedWith,
		CallbackInfo callback
	) {
		if (level instanceof ServerLevel serverLevel && state.is(BlockTags.OVERWORLD_NATURAL_LOGS)) {
			FastLeafDecay.onNaturalLogBroken(serverLevel, pos, player);
		}
	}
}
