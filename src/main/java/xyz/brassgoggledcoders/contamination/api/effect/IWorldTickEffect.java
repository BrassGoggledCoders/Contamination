package xyz.brassgoggledcoders.contamination.api.effect;

import net.minecraft.world.chunk.Chunk;

public interface IWorldTickEffect extends IContaminationEffect {
	public void triggerEffect(Chunk chunk);
}
