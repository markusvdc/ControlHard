package br.com.caphard;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.gameplay.FortuneHarvestBonus;
import br.com.caphard.gameplay.LodestoneCompassTeleport;
import net.fabricmc.api.ModInitializer;

public final class CapHard implements ModInitializer {
	public static final String MOD_ID = "caphard";

	@Override
	public void onInitialize() {
		CapHardConfig.load();
		FortuneHarvestBonus.register();
		LodestoneCompassTeleport.register();
	}
}
