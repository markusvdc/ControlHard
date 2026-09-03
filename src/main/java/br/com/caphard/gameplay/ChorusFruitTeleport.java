package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public final class ChorusFruitTeleport {
	private static final int MAX_RANGE = 5_000;
	private static final int MAX_ATTEMPTS = 32;

	private ChorusFruitTeleport() {
	}

	public static boolean shouldReplace(ItemStack stack, LivingEntity entity) {
		return CapHardConfig.safeChorusTeleport()
			&& stack.is(Items.CHORUS_FRUIT)
			&& entity instanceof ServerPlayer;
	}

	public static boolean teleport(ServerLevel level, ServerPlayer player) {
		RandomSource random = player.getRandom();
		for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
			BlockPos destination = findDestination(level, player.blockPosition(), random);
			if (destination == null) {
				continue;
			}

			Vec3 origin = player.position();
			if (player.isPassenger()) {
				player.stopRiding();
			}
			player.teleportTo(
				level,
				destination.getX() + 0.5,
				destination.getY(),
				destination.getZ() + 0.5,
				Set.of(),
				player.getYRot(),
				player.getXRot(),
				false
			);
			level.gameEvent(GameEvent.TELEPORT, origin, GameEvent.Context.of(player));
			level.playSound(
				null,
				player.getX(),
				player.getY(),
				player.getZ(),
				SoundEvents.CHORUS_FRUIT_TELEPORT,
				SoundSource.PLAYERS
			);
			player.resetFallDistance();
			player.resetCurrentImpulseContext();
			return true;
		}
		return false;
	}

	private static BlockPos findDestination(ServerLevel level, BlockPos origin, RandomSource random) {
		double angle = random.nextDouble() * Mth.TWO_PI;
		double distance = Math.sqrt(random.nextDouble()) * MAX_RANGE;
		int x = Mth.floor(origin.getX() + Math.cos(angle) * distance);
		int z = Mth.floor(origin.getZ() + Math.sin(angle) * distance);

		level.getChunk(x >> 4, z >> 4);
		return level.dimension() == Level.NETHER
			? findNetherDestination(level, x, z, random)
			: findSurfaceDestination(level, x, z);
	}

	private static BlockPos findSurfaceDestination(ServerLevel level, int x, int z) {
		int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
		BlockPos feet = new BlockPos(x, y, z);
		return isSafe(level, feet) ? feet : null;
	}

	private static BlockPos findNetherDestination(ServerLevel level, int x, int z, RandomSource random) {
		int minimumY = level.getMinY() + 1;
		int maximumY = level.getMinY() + level.getLogicalHeight() - 2;
		BlockPos selected = null;
		int validPositions = 0;
		for (int y = minimumY; y <= maximumY; y++) {
			BlockPos feet = new BlockPos(x, y, z);
			if (isSafe(level, feet) && random.nextInt(++validPositions) == 0) {
				selected = feet;
			}
		}
		return selected;
	}

	private static boolean isSafe(ServerLevel level, BlockPos feet) {
		if (!level.getWorldBorder().isWithinBounds(feet)) {
			return false;
		}
		BlockPos floorPos = feet.below();
		BlockState floor = level.getBlockState(floorPos);
		return floor.blocksMotion()
			&& floor.isCollisionShapeFullBlock(level, floorPos)
			&& !isDangerous(floor)
			&& isEmpty(level, feet)
			&& isEmpty(level, feet.above());
	}

	private static boolean isEmpty(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.getCollisionShape(level, pos).isEmpty()
			&& state.getFluidState().isEmpty()
			&& !isDangerous(state);
	}

	private static boolean isDangerous(BlockState state) {
		return state.is(BlockTags.FIRE)
			|| state.is(BlockTags.CAMPFIRES)
			|| state.is(BlockTags.LEAVES)
			|| state.is(Blocks.MAGMA_BLOCK)
			|| state.is(Blocks.CACTUS)
			|| state.is(Blocks.POWDER_SNOW)
			|| state.is(Blocks.SWEET_BERRY_BUSH)
			|| state.is(Blocks.WITHER_ROSE)
			|| state.is(Blocks.POINTED_DRIPSTONE);
	}
}
