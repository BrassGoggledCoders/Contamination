package xyz.brassgoggledcoders.contamination.events;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

//TODO Make Cancellable
public class ContaminationUpdateEvent extends Event {

	Chunk chunk;
	IContaminationType type;
	int contamination, delta;

	public ContaminationUpdateEvent(Chunk chunk, IContaminationType type, int contamination, int delta) {
		this.chunk = chunk;
		this.type = type;
		this.contamination = contamination;
		this.delta = delta;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public IContaminationType getType() {
		return type;
	}

}
