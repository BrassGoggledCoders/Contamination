package xyz.brassgoggledcoders.contamination;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;
import xyz.brassgoggledcoders.contamination.api.effect.*;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

@EventBusSubscriber(value = Side.CLIENT, modid = Contamination.MODID)
public class EventHandlerClient {
	@SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Pre event)
    {
		//FIXME This will not work yet because contamination level is not synced to the client.
		IContaminationHolder holder = Minecraft.getMinecraft().world.getChunk(Minecraft.getMinecraft().player.getPosition()).getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY, null);
		for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
			for(IContaminationEffect effect : type.getEffectSet(EnumEffectType.OVERLAY)) {
				if(holder.get(type) >= effect.getThreshold()) {
					((IOverlayEffect)effect).triggerEffect(event);
				}
			}
		}
    }
}
