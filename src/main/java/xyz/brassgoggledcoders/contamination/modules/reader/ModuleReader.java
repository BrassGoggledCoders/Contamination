package xyz.brassgoggledcoders.contamination.modules.reader;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.api.types.ContaminationTypeRegistry;
import xyz.brassgoggledcoders.contamination.api.types.IContaminationType;

@Module(Contamination.MODID)
public class ModuleReader extends ModuleBase {
	@Override
	public void registerItems(ConfigRegistry configRegistry, ItemRegistry itemRegistry) {
		itemRegistry.register(new ItemReader("debug"));
		for(IContaminationType type : ContaminationTypeRegistry.getAllTypes()) {
			itemRegistry.register(new ItemReader(type.getRegistryName()));
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		configure(getConfigRegistry());
		registerBlocks(getConfigRegistry(), getBlockRegistry());

		registerEntities(getConfigRegistry(), getEntityRegistry());
		getModuleProxy().ifPresent(proxy -> proxy.preInit(event));
	}

	// FIXME: Do later, to ensure types are registered first
	@Override
	public void afterModulesPreInit(FMLPreInitializationEvent event) {
		registerItems(getConfigRegistry(), getItemRegistry());
	}

	@Override
	public String getName() {
		return "Reader";
	}
}
