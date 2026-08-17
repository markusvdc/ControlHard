package br.com.caphard.client;

public final class HiddenInformationDetector {
	private static final ThreadLocal<Boolean> DETECTING = ThreadLocal.withInitial(() -> false);
	private static final ThreadLocal<Boolean> FORCED_SHIFT = new ThreadLocal<>();

	private HiddenInformationDetector() {
	}

	public static boolean isDetecting() {
		return DETECTING.get();
	}

	public static void begin() {
		DETECTING.set(true);
	}

	public static void end() {
		FORCED_SHIFT.remove();
		DETECTING.remove();
	}

	public static void forceShift(boolean pressed) {
		FORCED_SHIFT.set(pressed);
	}

	public static Boolean forcedShift() {
		return FORCED_SHIFT.get();
	}
}
