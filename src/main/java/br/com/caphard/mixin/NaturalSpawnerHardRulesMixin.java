package br.com.caphard.mixin;

import br.com.caphard.gameplay.HardRule;
import br.com.caphard.gameplay.HardRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerHardRulesMixin {
	private static final int WITCH_SPAWN_WEIGHT_MULTIPLIER = 8;
	private static final int ENDERMAN_SPAWN_WEIGHT_MULTIPLIER = 3;

	@Inject(method = "mobsAt", at = @At("RETURN"), cancellable = true)
	private static void caphard$increaseHardMonsterSpawnWeights(
		ServerLevel level,
		StructureManager structureManager,
		ChunkGenerator chunkGenerator,
		MobCategory category,
		BlockPos pos,
		Holder<Biome> biome,
		CallbackInfoReturnable<WeightedList<MobSpawnSettings.SpawnerData>> callback
	) {
		boolean increaseWitches = HardRules.isActive(level, HardRule.WITCH_WEIGHT);
		boolean increaseEndermen = HardRules.isActive(level, HardRule.ENDERMAN_WEIGHT);
		if (!increaseWitches && !increaseEndermen) {
			return;
		}

		WeightedList.Builder<MobSpawnSettings.SpawnerData> modifiedSpawns = WeightedList.builder();
		for (Weighted<MobSpawnSettings.SpawnerData> entry : callback.getReturnValue().unwrap()) {
			EntityType<?> type = entry.value().type();
			int multiplier = type == EntityTypes.WITCH && increaseWitches
				? WITCH_SPAWN_WEIGHT_MULTIPLIER
				: type == EntityTypes.ENDERMAN && increaseEndermen ? ENDERMAN_SPAWN_WEIGHT_MULTIPLIER : 1;
			modifiedSpawns.add(entry.value(), entry.weight() * multiplier);
		}
		callback.setReturnValue(modifiedSpawns.build());
	}
}
