package br.com.caphard.mixin.client;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.gameplay.HardRule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.DifficultyButtons;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DifficultyButtons.class)
public abstract class DifficultyButtonsMixin {
	@Inject(method = "create", at = @At("RETURN"))
	private static void caphard$removeDifficultyLock(
		Minecraft minecraft,
		Level level,
		Screen screen,
		CallbackInfoReturnable<DifficultyButtons> callback
	) {
		if (CapHardConfig.isRuleEnabled(HardRule.UNLOCK_DIFFICULTY)) {
			applyUnlockedState(minecraft, callback.getReturnValue());
		}
	}

	@Inject(method = "refresh", at = @At("RETURN"))
	private void caphard$keepDifficultyUnlocked(Minecraft minecraft, CallbackInfo callback) {
		if (CapHardConfig.isRuleEnabled(HardRule.UNLOCK_DIFFICULTY)) {
			applyUnlockedState(minecraft, (DifficultyButtons)(Object)this);
		}
	}

	private static void applyUnlockedState(Minecraft minecraft, DifficultyButtons buttons) {
		if (buttons.level().getLevelData().isDifficultyLocked()
			&& !buttons.level().getLevelData().isHardcore()
			&& minecraft.hasSingleplayerServer()
			&& minecraft.getConnection() != null) {
			minecraft.getConnection().send(new ServerboundLockDifficultyPacket(false));
		}
		buttons.lockButton().visible = false;
		buttons.lockButton().active = false;
		buttons.difficultyButton().active = !buttons.level().getLevelData().isHardcore()
			&& minecraft.hasSingleplayerServer();
	}
}
