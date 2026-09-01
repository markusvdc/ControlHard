package br.com.caphard.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
	@Accessor("LINES_SNIPPET")
	static RenderPipeline.Snippet caphard$linesSnippet() {
		throw new AssertionError();
	}

	@Invoker("register")
	static RenderPipeline caphard$register(RenderPipeline pipeline) {
		throw new AssertionError();
	}
}
