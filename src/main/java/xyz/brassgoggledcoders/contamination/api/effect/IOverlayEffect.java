package xyz.brassgoggledcoders.contamination.api.effect;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public interface IOverlayEffect extends IContaminationEffect {
	public void triggerEffect(RenderGameOverlayEvent.Pre event);
}
