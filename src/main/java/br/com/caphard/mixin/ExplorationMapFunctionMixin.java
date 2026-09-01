package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ExplorationMapFunction.class)
public abstract class ExplorationMapFunctionMixin {
	@Shadow
	@Final
	private TagKey<Structure> destination;

	@ModifyArg(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;findNearestMapStructure(Lnet/minecraft/tags/TagKey;Lnet/minecraft/core/BlockPos;IZ)Lnet/minecraft/core/BlockPos;"
		),
		index = 3
	)
	private boolean caphard$skipPreviouslyChosenTreasure(boolean vanillaValue) {
		return vanillaValue
			|| (CapHardConfig.uniqueTreasureMaps() && this.destination.equals(StructureTags.ON_TREASURE_MAPS));
	}
}
