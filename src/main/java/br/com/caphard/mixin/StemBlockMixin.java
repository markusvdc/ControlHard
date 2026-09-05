package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
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
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
		)
	)
	private boolean caphard$protectTilledSoil(BlockState soil, TagKey<Block> allowedSoils) {
		// Reject the soil before vanilla places either the fruit or its attached stem.
		if (CapHardConfig.protectTilledSoil() && soil.is(Blocks.FARMLAND)) {
			return false;
		}
		return soil.is(allowedSoils);
	}
}
