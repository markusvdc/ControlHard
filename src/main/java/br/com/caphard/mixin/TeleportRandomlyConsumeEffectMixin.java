package br.com.caphard.mixin;

import br.com.caphard.gameplay.ChorusFruitTeleport;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TeleportRandomlyConsumeEffect.class)
public abstract class TeleportRandomlyConsumeEffectMixin {
	@Inject(method = "apply", at = @At("HEAD"), cancellable = true)
	private void caphard$replaceChorusFruitTeleport(
		Level level,
		ItemStack stack,
		LivingEntity entity,
		CallbackInfoReturnable<Boolean> callbackInfo
	) {
		if (ChorusFruitTeleport.shouldReplace(stack, entity)) {
			callbackInfo.setReturnValue(ChorusFruitTeleport.teleport((ServerLevel) level, (ServerPlayer) entity));
		}
	}
}
