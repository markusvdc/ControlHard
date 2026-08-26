package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StemBlock.class)
abstract class StemBlockMixin {
	@Redirect(
		method = "randomTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
			ordinal = 0
		)
	)
	private boolean caphard$protectTilledSoil(ServerLevel level, BlockPos fruitPos, BlockState fruitState) {
		if (CapHardConfig.protectTilledSoil() && level.getBlockState(fruitPos.below()).is(Blocks.FARMLAND)) {
			return false;
		}
		return level.setBlockAndUpdate(fruitPos, fruitState);
	}
}
