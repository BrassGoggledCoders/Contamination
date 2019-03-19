package xyz.brassgoggledcoders.contamination.modules.journeymap;

import journeymap.client.api.*;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.util.PolygonHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.events.ContaminationUpdateEvent;

@ClientPlugin
public class ContaminationMapPlugin implements IClientPlugin {
	
	IClientAPI api;
	
	public ContaminationMapPlugin() {}

	@Override
	public void initialize(IClientAPI jmClientApi) {
		this.api = jmClientApi;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getModId() {
		return Contamination.MODID;
	}

	@Override
	public void onEvent(ClientEvent event) {
		
	}
	
	@SubscribeEvent
	public void onContaminationChanged(ContaminationUpdateEvent event) {
		try {
			api.show(new PolygonOverlay(getModId(), getModId() + ":" + event.getType().toString() + "_" + event.getChunk().toString(), 0, new ShapeProperties().setFillColor(event.getType().getColor()), PolygonHelper.createChunkPolygon(event.getChunk().x, 100/*TODO*/, event.getChunk().z)));
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
