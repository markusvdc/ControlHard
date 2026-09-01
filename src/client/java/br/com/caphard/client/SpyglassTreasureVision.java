package br.com.caphard.client;

import br.com.caphard.config.CapHardConfig;
import br.com.caphard.mixin.client.RenderPipelinesAccessor;
import br.com.caphard.mixin.client.RenderTypeAccessor;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public final class SpyglassTreasureVision {
	private static final int SEARCH_RADIUS = 20;
	private static final double SEARCH_RADIUS_SQUARED = SEARCH_RADIUS * SEARCH_RADIUS;
	private static final int OUTLINE_COLOR = 0xFFF9E44C;
	private static final float OUTLINE_WIDTH = 3.0F;
	private static final AtomicBoolean SCAN_SCHEDULED = new AtomicBoolean();
	private static volatile Set<BlockPos> treasurePositions = Set.of();
	private static RenderType treasureOutline;

	private SpyglassTreasureVision() {
	}

	public static void initialize() {
		ClientTickEvents.END_CLIENT_TICK.register(SpyglassTreasureVision::onClientTick);
		LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
			Minecraft minecraft = Minecraft.getInstance();
			if (!isActive(minecraft) || treasurePositions.isEmpty()) {
				return;
			}

			Vec3 camera = context.levelState().cameraRenderState.pos;
			PoseStack poseStack = context.poseStack();
			for (BlockPos pos : treasurePositions) {
				poseStack.pushPose();
				poseStack.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
				context.submitNodeCollector().submitShapeOutline(
					poseStack,
					Shapes.block(),
					treasureOutline(),
					OUTLINE_COLOR,
					OUTLINE_WIDTH,
					false
				);
				poseStack.popPose();
			}
		});
	}

	private static void onClientTick(Minecraft minecraft) {
		if (!isActive(minecraft)) {
			treasurePositions = Set.of();
			return;
		}

		if (!SCAN_SCHEDULED.compareAndSet(false, true)) {
			return;
		}

		var server = minecraft.getSingleplayerServer();
		var player = minecraft.player;
		if (server == null || player == null) {
			SCAN_SCHEDULED.set(false);
			treasurePositions = Set.of();
			return;
		}

		var playerId = player.getUUID();
		server.execute(() -> {
			try {
				ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerId);
				treasurePositions = serverPlayer == null ? Set.of() : findNearbyTreasures(serverPlayer);
			} finally {
				SCAN_SCHEDULED.set(false);
			}
		});
	}

	private static Set<BlockPos> findNearbyTreasures(ServerPlayer player) {
		ServerLevel level = player.level();
		Structure buriedTreasure = level.registryAccess()
			.lookupOrThrow(Registries.STRUCTURE)
			.getValueOrThrow(BuiltinStructures.BURIED_TREASURE);
		BlockPos center = player.blockPosition();
		int centerChunkX = center.getX() >> 4;
		int centerChunkZ = center.getZ() >> 4;
		Set<BlockPos> positions = new LinkedHashSet<>();

		for (int chunkX = centerChunkX - 2; chunkX <= centerChunkX + 2; chunkX++) {
			for (int chunkZ = centerChunkZ - 2; chunkZ <= centerChunkZ + 2; chunkZ++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);
				if (chunk == null) {
					continue;
				}

				for (var entry : chunk.getBlockEntities().entrySet()) {
					BlockPos pos = entry.getKey();
					if (player.distanceToSqr(Vec3.atCenterOf(pos)) > SEARCH_RADIUS_SQUARED) {
						continue;
					}

					boolean isBuriedTreasureChest = entry.getValue() instanceof ChestBlockEntity
						&& level.structureManager().getStructureWithPieceAt(pos, buriedTreasure).isValid();
					boolean isSuspiciousBlock = entry.getValue() instanceof BrushableBlockEntity
						&& (level.getBlockState(pos).is(Blocks.SUSPICIOUS_SAND)
							|| level.getBlockState(pos).is(Blocks.SUSPICIOUS_GRAVEL));
					if (isBuriedTreasureChest || isSuspiciousBlock) {
						positions.add(pos.immutable());
					}
				}
			}
		}

		return Set.copyOf(positions);
	}

	private static boolean isActive(Minecraft minecraft) {
		return CapHardConfig.spyglassTreasureVision()
			&& minecraft.player != null
			&& minecraft.player.isUsingItem()
			&& minecraft.player.getUseItem().is(Items.SPYGLASS)
			&& minecraft.hasSingleplayerServer();
	}

	private static RenderType treasureOutline() {
		if (treasureOutline == null) {
			RenderPipeline pipeline = RenderPipeline.builder(RenderPipelinesAccessor.caphard$linesSnippet())
				.withLocation(Identifier.fromNamespaceAndPath("caphard", "pipeline/treasure_outline"))
				.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
				.build();
			pipeline = RenderPipelinesAccessor.caphard$register(pipeline);
			RenderSetup setup = RenderSetup.builder(pipeline)
				.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.createRenderSetup();
			treasureOutline = RenderTypeAccessor.caphard$create("caphard_treasure_outline", setup);
		}
		return treasureOutline;
	}
}
