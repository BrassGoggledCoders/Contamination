package xyz.brassgoggledcoders.contamination.api.effect;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public interface IOverlayEffect extends IContaminationEffect {
	//N.B. Run in RenderGameOverlayEvent.Pre by default. If you want something else, you need a custom effect that you call from the event subscriber yourself
	public void triggerEffect(RenderGameOverlayEvent event);
}
