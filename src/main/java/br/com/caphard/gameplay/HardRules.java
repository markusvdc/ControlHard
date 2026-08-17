package br.com.caphard.gameplay;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

public final class HardRules {
	private HardRules() {
	}

	public static boolean isActive(Level level, HardRule rule) {
		return level.getDifficulty() == Difficulty.HARD && CapHardConfig.isRuleEnabled(rule);
	}
}
