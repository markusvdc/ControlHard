package br.com.caphard.mixin;

import br.com.caphard.config.CapHardConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
	private void caphard$allowMountedHorseThroughLeaves(
		BlockState state,
		BlockGetter level,
		BlockPos pos,
		CollisionContext context,
		CallbackInfoReturnable<VoxelShape> callback
	) {
		if (
			CapHardConfig.horseIgnoresLeaves()
				&& state.getBlock() instanceof LeavesBlock
				&& context instanceof EntityCollisionContext entityContext
				&& entityContext.getEntity() instanceof AbstractHorse horse
				&& horse.hasControllingPassenger()
		) {
			callback.setReturnValue(Shapes.empty());
		}
	}
}
