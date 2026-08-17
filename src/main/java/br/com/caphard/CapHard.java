package br.com.caphard;

import br.com.caphard.config.CapHardConfig;
import net.fabricmc.api.ModInitializer;

public final class CapHard implements ModInitializer {
	public static final String MOD_ID = "caphard";

	@Override
	public void onInitialize() {
		CapHardConfig.load();
	}
}
