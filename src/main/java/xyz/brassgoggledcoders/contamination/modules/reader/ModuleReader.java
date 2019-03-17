package xyz.brassgoggledcoders.contamination.modules.reader;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;

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
	public String getName() {
		return "Reader";
	}
}
