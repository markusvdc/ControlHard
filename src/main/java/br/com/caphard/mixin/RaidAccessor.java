package br.com.caphard.mixin;

import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Raid.class)
public interface RaidAccessor {
	@Accessor("numGroups")
	void caphard$setNumGroups(int numGroups);
}
