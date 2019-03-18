package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import com.teamacronymcoders.base.Base;
import com.teamacronymcoders.base.modulesystem.proxies.IModuleProxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;
import xyz.brassgoggledcoders.contamination.ContaminationMod;

public class ClientProxy implements IModuleProxy {
	@Override
	public void init(FMLInitializationEvent event) {
		Base.instance.getLibProxy().registerFluidModel(FluidRegistry.getFluid("algea").getBlock(),
				new ResourceLocation(ContaminationMod.MODID, "algea"));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
