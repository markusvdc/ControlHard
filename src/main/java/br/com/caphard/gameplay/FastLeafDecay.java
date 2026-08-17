package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class FastLeafDecay {
	private static final int CANOPY_RADIUS = 8;
	private static final int CUT_MEMORY_RADIUS = 12;
	private static final long CUT_MEMORY_TICKS = 20L * 60L;
	private static final int FORCED_DECAY_DEADLINE_TICKS = 20 * 5;
	private static final double EXTRA_DECAY_CHANCE = 57.0 / 4096.0;
	private static final double FAST_DECAY_CHANCE = 60.0 / 4096.0;

	private static final Map<ServerLevel, LevelState> LEVEL_STATES = new WeakHashMap<>();

	private FastLeafDecay() {
	}

	public static void onNaturalLogBroken(ServerLevel level, BlockPos pos, Player player) {
		if (!CapHardConfig.fasterLeafDecay()) {
			return;
		}

		LevelState levelState = LEVEL_STATES.computeIfAbsent(level, ignored -> new LevelState());
		long now = level.getGameTime();
		levelState.cutLogs.removeIf(cut -> now - cut.time > CUT_MEMORY_TICKS);
		levelState.cutLogs.add(new CutLog(pos.immutable(), now));
		for (Direction direction : Direction.values()) {
			if (level.getBlockState(pos.relative(direction)).is(BlockTags.OVERWORLD_NATURAL_LOGS)) {
				return;
			}
		}

		List<BlockPos> nearbyCuts = levelState.cutLogs.stream()
			.map(CutLog::pos)
			.filter(cutPos -> cutPos.distSqr(pos) <= CUT_MEMORY_RADIUS * CUT_MEMORY_RADIUS)
			.toList();
		List<BlockPos> remainingLogs = new ArrayList<>();
		int searchRadius = CANOPY_RADIUS + CUT_MEMORY_RADIUS;
		for (BlockPos candidate : BlockPos.betweenClosed(
			pos.offset(-searchRadius, -searchRadius, -searchRadius),
			pos.offset(searchRadius, searchRadius, searchRadius)
		)) {
			if (level.getBlockState(candidate).is(BlockTags.OVERWORLD_NATURAL_LOGS)) {
				remainingLogs.add(candidate.immutable());
			}
		}

		List<BlockPos> assignedLeaves = new ArrayList<>();
		for (BlockPos leafPos : BlockPos.betweenClosed(
			pos.offset(-CANOPY_RADIUS, -CANOPY_RADIUS, -CANOPY_RADIUS),
			pos.offset(CANOPY_RADIUS, CANOPY_RADIUS, CANOPY_RADIUS)
		)) {
			BlockState leafState = level.getBlockState(leafPos);
			if (!isNaturalLeaf(leafState)) {
				continue;
			}

			double removedDistance = nearestDistanceSquared(leafPos, nearbyCuts);
			double remainingDistance = nearestDistanceSquared(leafPos, remainingLogs);
			if (removedDistance <= remainingDistance) {
				BlockPos assignedPos = leafPos.immutable();
				assignedLeaves.add(assignedPos);
				schedule(level, assignedPos, leafState, FAST_DECAY_CHANCE, true);
			}
		}

		MagneticLeafDecayCompat.recordAssignedLeaves(level, assignedLeaves, player.getUUID());
	}

	public static boolean decayIfDue(ServerLevel level, BlockPos pos, BlockState state) {
		LevelState levelState = LEVEL_STATES.get(level);
		if (levelState == null) {
			return false;
		}

		ScheduledDecay scheduled = levelState.scheduledDecays.get(pos.asLong());
		if (scheduled == null) {
			return false;
		}

		boolean canDecay = isNaturalLeaf(state)
			&& (scheduled.forced || state.getValue(LeavesBlock.DISTANCE) == LeavesBlock.DECAY_DISTANCE);
		if (!CapHardConfig.fasterLeafDecay() || !canDecay) {
			levelState.scheduledDecays.remove(pos.asLong());
			return false;
		}

		if (level.getGameTime() < scheduled.dueTime) {
			long remainingTicks = scheduled.dueTime - level.getGameTime();
			level.scheduleTick(pos, state.getBlock(), (int)Math.min(Integer.MAX_VALUE, remainingTicks));
			return false;
		}

		if (!scheduled.prepared) {
			levelState.scheduledDecays.put(pos.asLong(), new ScheduledDecay(level.getGameTime() + 1L, scheduled.forced, true));
			level.scheduleTick(pos, state.getBlock(), 1);
			return false;
		}

		levelState.scheduledDecays.remove(pos.asLong());
		BlockState decayingState = state.setValue(LeavesBlock.DISTANCE, LeavesBlock.DECAY_DISTANCE);
		decayingState.randomTick(level, pos, level.getRandom());
		return true;
	}

	public static void scheduleVanillaDecay(ServerLevel level, BlockPos pos, BlockState state) {
		if (CapHardConfig.fasterLeafDecay()
			&& isNaturalLeaf(state)
			&& state.getValue(LeavesBlock.DISTANCE) == LeavesBlock.DECAY_DISTANCE) {
			schedule(level, pos, state, EXTRA_DECAY_CHANCE, false);
		}
	}

	public static void forgetVanillaDecay(ServerLevel level, BlockPos pos) {
		LevelState levelState = LEVEL_STATES.get(level);
		if (levelState != null) {
			ScheduledDecay scheduled = levelState.scheduledDecays.get(pos.asLong());
			if (scheduled != null && !scheduled.forced) {
				levelState.scheduledDecays.remove(pos.asLong());
			}
		}
	}

	private static void schedule(
		ServerLevel level,
		BlockPos pos,
		BlockState state,
		double chance,
		boolean forced
	) {
		LevelState levelState = LEVEL_STATES.computeIfAbsent(level, ignored -> new LevelState());
		long now = level.getGameTime();

		long packedPos = pos.asLong();
		ScheduledDecay existing = levelState.scheduledDecays.get(packedPos);
		if (existing != null && (existing.forced || !forced)) {
			return;
		}

		RandomSource random = level.getRandom();
		int delay = Math.max(1, (int)Math.ceil(Math.log1p(-random.nextDouble()) / Math.log1p(-chance)));
		if (forced) {
			delay = Math.min(delay, FORCED_DECAY_DEADLINE_TICKS);
		}
		levelState.scheduledDecays.put(packedPos, new ScheduledDecay(now + delay, forced, false));
		level.scheduleTick(pos, state.getBlock(), delay);
	}

	private static double nearestDistanceSquared(BlockPos origin, List<BlockPos> positions) {
		double nearest = Double.POSITIVE_INFINITY;
		for (BlockPos pos : positions) {
			nearest = Math.min(nearest, origin.distSqr(pos));
		}
		return nearest;
	}

	private static boolean isNaturalLeaf(BlockState state) {
		return state.getBlock() instanceof LeavesBlock
			&& state.is(BlockTags.LEAVES)
			&& !state.getValue(LeavesBlock.PERSISTENT);
	}

	private record CutLog(BlockPos pos, long time) {
	}

	private record ScheduledDecay(long dueTime, boolean forced, boolean prepared) {
	}

	private static final class LevelState {
		private final List<CutLog> cutLogs = new ArrayList<>();
		private final Map<Long, ScheduledDecay> scheduledDecays = new HashMap<>();
	}
}
