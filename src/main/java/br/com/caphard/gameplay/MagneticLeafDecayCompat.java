package br.com.caphard.gameplay;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

final class MagneticLeafDecayCompat {
	private static final String MAGNETIC_MOD_ID = "magnetic";

	private MagneticLeafDecayCompat() {
	}

	static void recordAssignedLeaves(ServerLevel level, List<BlockPos> positions, UUID playerUuid) {
		if (positions.isEmpty() || !FabricLoader.getInstance().isModLoaded(MAGNETIC_MOD_ID)) {
			return;
		}

		try {
			Object tracker = level.getClass().getMethod("getLeafDecayTracker").invoke(level);
			long timeout = configuredTimeout();
			Method record = tracker.getClass().getMethod("record", Iterable.class, UUID.class, long.class);
			record.invoke(tracker, positions, playerUuid, timeout);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Magnetic leaf decay compatibility failed", exception);
		}
	}

	private static long configuredTimeout() throws ReflectiveOperationException {
		Class<?> configKt = Class.forName("dev.nyon.magnetic.config.ConfigKt");
		Object config = configKt.getMethod("getConfig").invoke(null);
		Object leafDecay = config.getClass().getMethod("getLeafDecay").invoke(config);
		return (long)leafDecay.getClass().getMethod("getAbilityTimeout").invoke(leafDecay);
	}
}
