package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import com.teamacronymcoders.base.Base;
import com.teamacronymcoders.base.modulesystem.proxies.IModuleProxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.*;
import xyz.brassgoggledcoders.contamination.Contamination;

public class ClientProxy implements IModuleProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityFertilizerCreeper.class, new IRenderFactory<EntityFertilizerCreeper>(){
            public Render<EntityFertilizerCreeper> createRenderFor(RenderManager manager) {return new RenderFertilizerCreeper(manager);}
});
	}
		
	@Override
	public void init(FMLInitializationEvent event) {
		Base.instance.getLibProxy().registerFluidModel(FluidRegistry.getFluid("algea").getBlock(),
				new ResourceLocation(Contamination.MODID, "algea"));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
