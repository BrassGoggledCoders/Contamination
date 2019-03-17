package xyz.brassgoggledcoders.contamination.modules.reader;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.brassgoggledcoders.contamination.ContaminationMod;
import xyz.brassgoggledcoders.contamination.api.ContaminationTypeRegistry;

@Module(ContaminationMod.MODID)
public class ModuleReader extends ModuleBase {
	@Override
    public void registerItems(ConfigRegistry configRegistry, ItemRegistry itemRegistry) {
		itemRegistry.register(new ItemReader(-1));
		for(int pos = 0; pos < ContaminationTypeRegistry.getNumberOfTypes(); pos++) {
			itemRegistry.register(new ItemReader(pos));
		}
	}
	
	@Override
    public void preInit(FMLPreInitializationEvent event) {
        this.configure(this.getConfigRegistry());
        this.registerBlocks(this.getConfigRegistry(), this.getBlockRegistry());
        
        this.registerEntities(this.getConfigRegistry(), this.getEntityRegistry());
        this.getModuleProxy().ifPresent(proxy -> proxy.preInit(event));
    }

	//Do later, to ensure types are registered first
    @Override
    public void afterModulesPreInit(FMLPreInitializationEvent event) {
    	this.registerItems(this.getConfigRegistry(), this.getItemRegistry());
    }

	@Override
	public String getName() {
		return "Reader";
	}
}
